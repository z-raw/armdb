package imdb

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import io.delta.tables.DeltaTable
import io.delta.tables._


/**
 * ETL Job for cleaning and transforming the imdb datasets and loading them into a postgres database.
 *
 * Null values are represented as \N in the datasets.
 *
 * For malformed data:
 *  - we will use the permissive mode
 *  - store the malformed rows in a separate table
 */
object ImdbETL extends App {

  // creating a SparkSession
  val spark = SparkSession.builder()
    .appName("Imdb ETL")
    .config("spark.master", "local")
    .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
    .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
    .getOrCreate()

  // tconst length should be less than 20

  val titlesSchema = StructType(Array(
    StructField("tconst", StringType, false),
    StructField("titleType", StringType, false),
    StructField("primaryTitle", StringType, false),
    StructField("originalTitle", StringType, true),
    StructField("isAdult", IntegerType, false),
    StructField("startYear", IntegerType, false),
    StructField("endYear", IntegerType, true),
    StructField("runtimeMinutes", IntegerType, true),
    StructField("genres", StringType, true),
    StructField("_corrupt_record", StringType, true)
  ))

  // Create the titles table
  spark.sql(
    """
   CREATE TABLE IF NOT EXISTS titles (
         id UUID DEFAULT gen_random_uuid(),
         tconst VARCHAR(20) UNIQUE NOT NULL,
         type VARCHAR(40),
         primary_title VARCHAR(800) NOT NULL,
         original_title VARCHAR(800),
         is_adult BOOLEAN,
         start_year INT,
         end_year INT,
         runtime_minutes INT,
         genres VARCHAR(800),
         PRIMARY KEY (id)
     );
   """)


  val titlesDF = spark.read
    .format("csv")
    .schema(titlesSchema)
    .option("header", "true")
    .option("nullValue", "\\N")
    .option("delimiter", "\t")
    .option("mode", "PERMISSIVE")
    .option("badRecordsPath", "badRecords/")
    .load("title.basics.tsv")

  titlesDF.show()

  //write DF to Postgres table
  titlesDF
    // filter corrupted records
    .filter(col("_corrupt_record").isNull)
    .filter(len(col("tconst")) <= 20)
    .filter(len(col("titleType")) <= 40)
    .filter(len(col("primaryTitle")) <= 800)
    .filter(len(col("originalTitle")) <= 800)
    .filter(len(col("genres")) <= 800)

    // drop column _corrupt_record
    .drop("_corrupt_record")
    .filter("startYear >= 2021")
    .withColumnsRenamed(Map("titleType" -> "type", "primaryTitle" -> "primary_title", "originalTitle" -> "original_title", "isAdult" -> "is_adult", "startYear" -> "start_year", "endYear" -> "end_year", "runtimeMinutes" -> "runtime_minutes"))
    .withColumn("is_adult", col("is_adult").cast("boolean"))
    .write
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://localhost:5432/armdb")
    .option("dbtable", "titles")
    .option("user", "postgres")
    .option("password", "")
    .mode("append")
    .save()

  //   write corrupted records to csv file
  titlesDF
    .filter(col("_corrupt_record").isNotNull or len(col("tconst")) > 20 or len(col("titleType")) > 40 or len(col("primaryTitle")) > 800 or len(col("originalTitle")) > 800 or len(col("genres")) > 800)
    .write
    .format("csv")
    // delimiter is tab
    .option("delimiter", "\t")
    .option("header", "true")
    .mode("overwrite")
    .save("badRecords/titles/")


  //   Casts
  //
  //   Create casts table
  //   casts tsv fields: nconst  primaryName     birthYear       deathYear       primaryProfession       knownForTitles

  val castsSchema = StructType(Array(
    StructField("nconst", StringType, false),
    StructField("primaryName", StringType, false),
    StructField("birthYear", IntegerType, true),
    StructField("deathYear", IntegerType, true),
    StructField("primaryProfession", StringType, true),
    StructField("knownForTitles", StringType, true),
    StructField("_corrupt_record", StringType, true)
  ))

  val castsDF = spark.read
    .format("csv")
    .schema(castsSchema)
    .option("header", "true")
    .option("nullValue", "\\N")
    .option("delimiter", "\t")
    .option("mode", "PERMISSIVE")
    .option("badRecordsPath", "badRecords/")
    .load("name.basics.tsv")

  castsDF.show()

  // create casts table
  spark.sql(
    """
     CREATE TABLE IF NOT EXISTS casts (
           id UUID DEFAULT gen_random_uuid(),
           nconst VARCHAR(20) UNIQUE NOT NULL,
           primary_name VARCHAR(800) NOT NULL,
           birth_year INT,
           death_year INT,
           primary_profession VARCHAR(800),
           known_for_titles VARCHAR(800),
           PRIMARY KEY (id)
       );
   """)

  //   write castsDF to Postgres table

  castsDF
    .filter("_corrupt_record is null")
    .filter(len(col("nconst")) <= 20)
    .filter(len(col("primaryName")) <= 800)
    .filter(len(col("primaryProfession")) <= 800)
    .filter(len(col("knownForTitles")) <= 800)
    .drop("_corrupt_record")
    .withColumnsRenamed(Map("primaryName" -> "primary_name", "birthYear" -> "birth_year", "deathYear" -> "death_year", "primaryProfession" -> "primary_profession", "knownForTitles" -> "known_for_titles"))
    .withColumn("birth_year", col("birth_year").cast("int"))
    .withColumn("death_year", col("death_year").cast("int"))
    .withColumn("primary_profession", col("primary_profession").cast("varchar(800)"))
    .withColumn("known_for_titles", col("known_for_titles").cast("varchar(800)"))
    .write
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://localhost:5432/armdb")
    .option("dbtable", "casts")
    .option("user", "postgres")
    .option("password", "")
    .mode("append")
    .save()

  //write corrupted castsDF to csv file
  castsDF
    .filter(col("_corrupt_record").isNotNull or len(col("nconst")) > 20 or len(col("primaryName")) > 800 or len(col("primaryProfession")) > 800 or len(col("knownForTitles")) > 800)
    .write
    .format("csv")
    // delimiter is tab
    .option("delimiter", "\t")
    .option("header", "true")
    .mode("overwrite")
    .save("badRecords/casts/")

  //
  //   Create the JOIN table for titles and casts
  //   JOIN table tsv: tconst  ordering        nconst  category        job     characters

  val principalsSchema = StructType(Array(
    StructField("tconst", StringType, false),
    StructField("ordering", IntegerType, false),
    StructField("nconst", StringType, false),
    StructField("category", StringType, false),
    StructField("job", StringType, true),
    StructField("characters", StringType, true),
    StructField("_corrupt_record", StringType, true)
  ))

  val principalsDF = spark.read
    .format("csv")
    .schema(principalsSchema)
    .option("header", "true")
    .option("nullValue", "\\N")
    .option("delimiter", "\t")
    .option("mode", "PERMISSIVE")
    .option("badRecordsPath", "badRecords/")
    .load("title.principals.tsv")

  principalsDF.show()

  spark.sql(
    """
     CREATE TABLE IF NOT EXISTS principals (
           titleId UUID,
           castId UUID,
           tconst VARCHAR(20)  NOT NULL,
           ordering INT NOT NULL,
           nconst VARCHAR(20) NOT NULL,
           category VARCHAR(40) NOT NULL,
           job VARCHAR(40),
           characters VARCHAR(40)
       );
   """)

  principalsDF
    .filter(col("_corrupt_record").isNull)
    .filter(len(col("tconst")) <= 20)
    .filter(len(col("nconst")) <= 20)
    .filter(len(col("category")) <= 40)
    .filter(col("job").isNull or len(col("job")) <= 40)
    .filter(col("characters").isNull or len(col("characters")) <= 40)
    .drop("_corrupt_record")
    .write
    .format("jdbc")
    .option("driver", "org.postgresql.Driver")
    .option("url", "jdbc:postgresql://localhost:5432/armdb")
    .option("dbtable", "principals")
    .option("user", "postgres")
    .option("password", "")
    .mode("append")
    .save()


  // write corrupted principalsDF to csv file
  principalsDF
    .filter(col("_corrupt_record").isNotNull or len(col("tconst")) > 20 or len(col("nconst")) > 20 or len(col("category")) > 40 or len(col("job")) > 40 or len(col("characters")) > 40)
    .write
    .format("csv")
    // delimiter is tab
    .option("delimiter", "\t")
    .option("header", "true")
    .mode("overwrite")
    .save("badRecords/principals/")


}
