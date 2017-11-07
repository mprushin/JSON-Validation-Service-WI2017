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
    * Recursive implementation of removeNullValues function
    * @param json Json object
    * @return new Json object with null nodes removed
    */
  def removeNullValuesRecursive(json: Json): Json = {
    json.hcursor.keys.get.foldLeft(json: Json)((json, key) => deleteIfNullValue(json, key))
  }

  /**
    * Helper function which deletes node from Json if value is null and if value is other json object runs itself for each key of this object
    * @param json Json object
    * @param key Key to check
    * @return new Json object with null nodes removed
    */
  def deleteIfNullValue(json: Json, key: String): Json = {
    val cursor = json.hcursor.downField(key)
    cursor.keys match {
      case Some(keys) => {
        keys.foldLeft(json: Json)((json, key) => cursor.withFocus(json => deleteIfNullValue(json, key)).top.get)
      }
      case None => {
        if(cursor.focus.get.isNull){
          cursor.delete.top.getOrElse(Json.Null)
        }
        else{
          cursor.top.get
        }
      }
    }
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
