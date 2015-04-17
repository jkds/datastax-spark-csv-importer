package com.datastax.demo

import org.apache.spark.{Logging, SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import com.datastax.spark.connector._
import com.databricks.spark.csv._

import scala.annotation.tailrec
import scala.reflect.io.Path


/**
 * Simple Application that runs Spark either locally or remotely using dse-submit
 * prompts for an input CSV file and then saves that to a cassandra table
 */
object CsvImporter extends App with ArgHelper with Logging {

  val importFile = getArgOrDefault("csv.file", getFile("Please enter the path of the file you wish to import > "))
  logInfo(s"Commencing Import of file $importFile")

  //Create Spark config with sensible defaults
  val conf = new SparkConf()
    .setMaster(getArgOrDefault("spark.master","local[2]"))
    .setAppName("spark-cass-csv-importer")
    .set("spark.executor.memory", getArgOrDefault("executor.memory", "512m"))
    .set("spark.default.parallelism", getArgOrDefault("processing.cores", "2"))
    .set("spark.cassandra.connection.host", getArgOrDefault("cassandra.host", "127.0.0.1"))

  val sc = new SparkContext(conf)

  val sparkSQL = new SQLContext(sc)

  val geographyLevelsCsv = sparkSQL.csvFile(importFile)

  val mappedForCassandra = geographyLevelsCsv.map {
    row =>
      (row.getAs[Int](0),
        row.getString(1),
        row.getString(2),
        row.getString(3).toBoolean)
  }

  mappedForCassandra.saveToCassandra("demo","products",SomeColumns("id","code","description","disabled"))

  sc.stop

  logInfo("Import Completed!")

  @tailrec
  def getFile(prompt : String) : String = {
    val file = readFromStdIn(prompt)
    if(Path.string2path(file).exists) {
      file
    } else {
      getFile(prompt)
    }
  }



}
