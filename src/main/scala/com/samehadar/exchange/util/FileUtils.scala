package com.samehadar.exchange.util

import java.io.{File, PrintWriter, BufferedReader, InputStreamReader}
import java.util.stream.Collectors

object FileUtils {

    def saveToWorkingDirectory[T](objects: List[T], resourceFile: String): Unit = {
        val resourcesPath = System.getProperty("user.dir") + """\src\main\resources\""".replace("""\""", System.getProperty("file.separator"))

        val writer = new PrintWriter(new File(resourcesPath + resourceFile))
        writer.print(objects.mkString("\n"))
        writer.close()
    }

    def saveToTargetDirectory[T](objects: List[T], resourceFile: String): Unit = {
        val in = getClass().getResourceAsStream("/result.txt");
        val reader = new BufferedReader(new InputStreamReader(in));
        val writer = new PrintWriter(new File(reader.lines().collect(Collectors.joining())))
        writer.print(objects.mkString("\n"))
        writer.close()
    }

}
