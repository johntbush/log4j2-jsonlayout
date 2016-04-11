package com.trax.platform.logging

import com.typesafe.scalalogging.LazyLogging
import org.junit.Test
import org.slf4j.MDC

/**
  * Created by johnbush on 4/10/16.
  */
class JsonLayoutTest extends LazyLogging {
  @Test
  def test1():Unit = {
    MDC.put("first", "Dorothy")
    MDC.put("second", "Wizard")
    MDC.put("third", "ToTo")
    logger.info("logging hi world")
    println("Hello, world!")
  }
}
