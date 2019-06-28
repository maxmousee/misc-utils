import scala.io.Source
import java.nio.file.{FileSystems, Files}

import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.util.Try

object LinesCounter {

  def getLOCForSnapshot(dir: String): Int = {
    var loc = 0
    val snapshotPath = FileSystems.getDefault.getPath(dir)
    IteratorHasAsScala(Files.walk(snapshotPath).iterator()).asScala.filter(Files.isRegularFile(_)).foreach {
      file =>
        Try {
          val bufferedSource = Source.fromFile(file.toString)
          loc += bufferedSource.getLines().length
          bufferedSource.close()
        }
    }
    loc
  }

  def main(args: Array[String]) {
    try {
      val baseDir = args(0)
      println("Counting lines in text files in dir: " + baseDir)
      val startTime = System.currentTimeMillis()
      val loc = getLOCForSnapshot(baseDir)
      val elapsedTime = System.currentTimeMillis() - startTime
      println("Lines in text files: " + loc)
      println("Elapsed time: " + elapsedTime + " ms")
    } catch {
      case _: Throwable => println("Missing path argument...")
        println("Usage: scala LOCCounter.scala <directory>")
    }
  }
}

