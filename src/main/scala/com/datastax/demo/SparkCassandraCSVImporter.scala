package com.datastax.demo

import com.databricks.spark.csv._
import com.datastax.demo.mapper.CSVMapAndSave
import com.datastax.demo.util.ArgHelper
import org.apache.spark.sql.SQLContext
import org.apache.spark.{Logging, SparkConf, SparkContext}

import scala.annotation.tailrec
import scala.reflect.io.Path

/**
 * Abstract class that is a base class for importing data from CSV files.
 * See the example template class ProductCSVMapAndSave for details on how
 * to implement your own.
 * Created on 19/04/2015.
 */
abstract class SparkCassandraCSVImporter(val args : Array[String]) extends ArgHelper with Logging {
  //Dependency Requirement
  required : CSVMapAndSave =>

  def doImport() = {

    val importFile = getArgOrDefault("csv.file", getFile("Please enter the path of the file you wish to import > "))

    logInfo(s"Commencing Import of file $importFile")

    //Create Spark config with sensible defaults
    val conf = new SparkConf()
      .setMaster(getArgOrDefault("spark.master", "local[2]"))
      .setAppName("spark-cass-csv-importer")
      .set("spark.executor.memory", getArgOrDefault("executor.memory", "512m"))
      .set("spark.default.parallelism", getArgOrDefault("processing.cores", "2"))
      .set("spark.cassandra.connection.host", getArgOrDefault("cassandra.host", "127.0.0.1"))

    val sc = new SparkContext(conf)

    val sparkSQL = new SQLContext(sc)

    val csvRDD = sparkSQL.csvFile(importFile)

    mapAndSave(csvRDD)

    sc.stop

    logInfo("Import Completed!")

  }

  @tailrec
  private def getFile(prompt : String) : String = {
    val file = readFromStdIn(prompt)
    if(Path.string2path(file).exists) {
      file
    } else {
      getFile(prompt)
    }
  }

}
