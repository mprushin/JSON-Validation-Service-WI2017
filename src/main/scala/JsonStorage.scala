import java.io.{File, FileNotFoundException, PrintWriter}

import io.circe.{Json, Printer}

import scala.io.Source

object JsonStorage {
  val storagePath = "C:\\SchemaStorage\\"

  def saveSchema(schemaString: Json, schemaId: String): Unit = {
    val writer = new PrintWriter(new File(storagePath + schemaId + ".sch"))
    writer.write(schemaString.pretty(Printer.spaces2))
    writer.close()
  }

  def getSchema(schemaId: String): Option[Json] = {
    try {
      Option(JsonUtils.loadJson(Source.fromFile(storagePath + schemaId + ".sch").mkString))
    } catch {
      case e: FileNotFoundException => None
    }
  }

}
