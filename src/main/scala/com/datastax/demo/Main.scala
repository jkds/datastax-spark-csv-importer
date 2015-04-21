package com.datastax.demo

import com.datastax.demo.mapper.ProductCSVMapAndSave

/**
 * Created on 19/04/2015.
 */
object Main extends App {

  val airportsMapper = new ProductCSVMapAndSave(args)
  airportsMapper.doImport()

}
