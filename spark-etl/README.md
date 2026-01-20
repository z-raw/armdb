# Spark Essentials

This repository contains code for cleaning and data wrangling on the IMDb dataset using Spark and Scala.
Spark makes it easy to process large datasets in parallel.

## How to run the ETL job 

- open with IntelliJ as an SBT project or VS Code with the Scala Metals plugin
- run `docker compose up` 
- in another terminal window, navigate to `spark-cluster/` 
- build the Docker-based Spark cluster with
```
chmod +x build-images.sh
./build-images.sh
```
- before running the ETL job, start the Spark cluster, go to the `spark-cluster` directory.
- Run `docker compose up --scale spark-worker=3` to spin up the Spark containers with 3 worker nodes

