import io.circe._
import io.circe.parser._

object JsonUtils {
  /**
    * Remove nodes with null values from json object
    * @param json Json object
    * @return new Json object with null values deleted
    */
  def removeNullValues(json: Json): Json = {
    loadJson(json.pretty(Printer.spaces2.copy(dropNullValues = true)))
  }

  /**
    * Loads Json from string
    * @param jsonString - String to load
    * @return Json object
    */
  def loadJson(jsonString: String): Json = {
    parse(jsonString).getOrElse(Json.Null)
  }

}
