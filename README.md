# Example Cassandra CSV Import w.Spark

----

I've been asked a few times over the last couple of months if there's a way to import CSV files into Cassandra without too much work. There are a number of different strategies but I thought I would look at using Spark to do this.
The [Spark CSV library](https://github.com/databricks/spark-csv) allows the import of CSV files to create Spark RDDs. Combined with the [Datastax Spark Cassandra Connector](https://github.com/datastax/spark-cassandra-connector) this makes it simple to import with very little code required.


This example prompts you for the name of a csv file from the command line and imports it into Cassandra. The example currently writes to a CQL table called `products` with the following columns:

| id [int]   | code [text]  | description [text]       | enabled[boolean] |
| ---------- | :---------- | :--------------- | :------ |
| 1          | FORKAN      | Fork 'andles     | true    |
| 2          | FOOTPU      | Foot Pumps       | true    |
| ...        | ...         | ...              | ...

Obviously you can customise this to suit your needs.

## Setup

To get started create the keyspace for the demo using cqlsh and the provided DDL script against your chosen Cassandra instance (local or remote):

    cqlsh -h {cassandra-host} -f create-demo.sql
    
Once completed successfully you can now execute the program and import data.

**NOTE:** In order to build the project [_sbt_](http://www.scala-sbt.org/) must be installed. 

## Executing
In order to run from the command line enter sbt: 

	$ sbt
   
   
Then once in sbt run:

	> clean
   
   	> compile
   
   	> run {csv.file=?} {spark.master=?} {cassandra.host=?} {spark.executor.memory=?} {processing.cores=?}
  	
   	
If you did not specify the path of the csv file to you'll then be prompted to enter the path of the file
    
    Please enter the path of the file you wish to import > _
    
There is an example file included with the project called `sample-products.csv` enter this at the prompt and press return.

The default parameters configured within the app are as follows:

| Name         | default value |
| ------------ | ------------- |
| spark.master | local[2] |
| cassandra.host | 127.0.0.1 |
| executor.memory | 512m |
| processing.cores | 2 |
| csv.file | N/A (prompted if not present) |

You may override these on the command line as indicated above.

## Results
Once the program has completed successfully it you should see the following when performing a select:

    Connected to SparkCluster at localhost:9160.
    [cqlsh 4.1.1 | Cassandra 2.0.11.83 | DSE 4.6.0 | CQL spec 3.1.1 | Thrift protocol 19.39.0]
    Use HELP for help.
    cqlsh> select * from demo.products;
    
     id | code  | description   | disabled
    ----+-------+---------------+----------
      5 | DEAPA |   Dead Parrot |    False
      1 | CHSGR | Cheese Grater |    False
      2 | CANOP |    Tin Opener |    False
      4 | FORCA |   Fork Handle |    False
      3 | TEAST |  Foot Pumps   |    False

    (5 rows)

If this is not the result you see then check the application output (stdout) for errors.

## Packaging

If you wish to run the application standalone without sbt then run the following sbt command:

    sbt universal:packageBin
    
This will produce a zip file under the `target/universal` directory called `spark-csv-cassandra-import-0.1.zip` unzip this file to
a directory of your choice and then from that directory execute the following:

_Linux_

    bin/spark-csv-cassandra-import {cmd-line-args}
    
_Windows_ (untested)

    bin/spark-csv-cassandra-import.bat {cmd-line-args}

Again if you do not specify the name of the CSV file you wish to import then you'll be prompted to enter it.