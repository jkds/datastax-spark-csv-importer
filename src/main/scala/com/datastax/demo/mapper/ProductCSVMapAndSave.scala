package com.datastax.demo.mapper

import com.datastax.demo.SparkCassandraCSVImporter
import com.datastax.spark.connector._
import org.apache.spark.sql.SchemaRDD

/**
 * This template class shows how you can map an RDD and export it to Cassandra with the appropriate column names for
 * the target table
 *
 * Created on 19/04/2015.
 */
class ProductCSVMapAndSave(override val args : Array[String]) extends SparkCassandraCSVImporter(args) with CSVMapAndSave {

  override def mapAndSave(rdd: SchemaRDD): Unit = {
    rdd.map { row =>
      (row.getAs[Int](0),
        row.getString(1),
        row.getString(2),
        row.getString(3).toBoolean)
    }.saveToCassandra("demo","products",SomeColumns("id","code","description","disabled"))

  }


}
