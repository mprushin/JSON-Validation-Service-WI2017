import io.circe.{Json, Printer}
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class JsonStorage(val dbConfig: String) {
  val db = Database.forConfig(dbConfig)

  class Jsons(tag: Tag) extends Table[(String, String)](tag, "JSONS") {
    def id = column[String]("ID", O.PrimaryKey)

    def data = column[String]("DATA")

    def * = (id, data)
  }

  val jsons = TableQuery[Jsons]
  initDB()

  /**
    * Initialize a DataBase schema
    */
  def initDB(): Unit = {
    def localTables: DBIO[Vector[String]] =
      MTable.getTables("JSONS").map { ts =>
        ts.map(_.name.name).sorted
      }

    val tables = Await.result(db.run(localTables), Duration.Inf)

    if (!tables.contains("JSONS")) {
      Await.result(db.run(jsons.schema.create), Duration.Inf)
    }
  }

  /**
    * Saves schema to the filesystem (storage path should be created beforehand
    *
    * @param schema   - Json object with schema to save
    * @param schemaId - Json identifier (if there is a schema with such identifier - it will be replaced with new one
    */
  def saveSchema(schema: Json, schemaId: String): Unit = {
    val insertOrUpdateAction = jsons.map(j => (j.id, j.data)).insertOrUpdate((schemaId, schema.pretty(Printer.noSpaces)))
    val insertFuture = db.run(insertOrUpdateAction)
    Await.result(insertFuture, Duration.Inf)
  }

  /**
    * Reads schema form filesystem by id
    *
    * @param schemaId - schema identifier
    * @return Json oblect with schema
    */
  def getSchema(schemaId: String): Option[Json] = {
    val getFuture: Future[Seq[(String, String)]] = db.run(jsons.filter(_.id === schemaId).result)
    val getResult = Await.result(getFuture, Duration.Inf)
    getResult.toList match {
      case head :: Nil => Option(JsonUtils.loadJson(head._2))
      case Nil => None
    }
  }

}

object JsonStorage extends JsonStorage("h2loc1")