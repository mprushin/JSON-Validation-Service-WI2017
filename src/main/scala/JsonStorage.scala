import java.io.{File, FileNotFoundException, PrintWriter}

import io.circe.{Json, Printer}

import scala.io.Source

object JsonStorage {
  val storagePath = "C:\\SchemaStorage\\"

  /**
    * Saves schema to the filesystem (storage path should be created beforehand
    * @param schema - Json object with schema to save
    * @param schemaId - Json identifier (if there is a schema with such identifier - it will be replaced with new one
    */
  def saveSchema(schema: Json, schemaId: String): Unit = {
    val writer = new PrintWriter(new File(storagePath + schemaId + ".sch"))
    writer.write(schema.pretty(Printer.spaces2))
    writer.close()
  }

  /**
    * Reads schema form filesystem by id
    * @param schemaId - schema identifier
    * @return Json oblect with schema
    */
  def getSchema(schemaId: String): Option[Json] = {
    try {
      Option(JsonUtils.loadJson(Source.fromFile(storagePath + schemaId + ".sch").mkString))
    } catch {
      case e: FileNotFoundException => None
    }
  }

}
