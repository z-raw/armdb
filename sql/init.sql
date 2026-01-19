
CREATE TABLE IF NOT EXISTS titles (
            id SERIAL PRIMARY KEY,
            download_date_id INT,
            tconst VARCHAR(20) UNIQUE NOT NULL,
            type VARCHAR(40),
            primary_title VARCHAR(800) NOT NULL,
            original_title VARCHAR(800),
            is_adult BOOLEAN,
            start_year SMALLINT,
            end_year SMALLINT,
            runtime_minutes SMALLINT,
            genre_1 VARCHAR(25),
            genre_2 VARCHAR(25),
            genre_3 VARCHAR(25)
        );

CREATE TABLE IF NOT EXISTS casts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    age SMALLINT,
    is_alive BOOLEAN,
    nconst VARCHAR(15) NOT NULL
);



CREATE TABLE IF NOT EXISTS titles_casts (
    title_id INT REFERENCES titles (id),
    cast_id INT REFERENCES casts (id),
    character VARCHAR(300)
);