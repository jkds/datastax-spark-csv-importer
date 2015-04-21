package com.datastax.demo.mapper

import org.apache.spark.sql.SchemaRDD

/**
 * Created on 19/04/2015.
 */
trait CSVMapAndSave {

  def mapAndSave(rdd : SchemaRDD)


}
