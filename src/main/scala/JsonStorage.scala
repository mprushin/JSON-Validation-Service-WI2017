import java.io.{File, FileNotFoundException, PrintWriter}

import scala.io.Source

class JsonStorage {
  val storagePath = "C:\\SchemaStorage\\"

  def saveSchema(schemaString: String, schemaId: String): Unit = {
    val writer = new PrintWriter(new File(storagePath + schemaId + ".sch"))
    writer.write(schemaString)
    writer.close()
  }

  def getSchema(schemaId: String): Option[String] = {
    try {
      Option(Source.fromFile(storagePath + schemaId + ".sch").mkString)
    } catch {
      case e: FileNotFoundException => None
    }
  }

}
