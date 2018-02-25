package com.samehadar.exchange.util

import java.io.{File, PrintWriter}

object FileUtils {

  def saveToWorkingDirectory[T](objects: List[T], resourceFile: String): Unit = {
    val resourcesPath = System.getProperty("user.dir") + """\src\main\resources\""".replace("""\""", System.getProperty("file.separator"))

    val writer = new PrintWriter(new File(resourcesPath + resourceFile))
    writer.print(objects.mkString("\n"))
    writer.close()
  }

  def saveToTargetDirectory[T](objects: List[T], resourceFile: String): Unit = {
    val writer = new PrintWriter(new File(getClass.getResource("/" + resourceFile).toURI))
    writer.print(objects.mkString("\n"))
    writer.close()
  }

}
