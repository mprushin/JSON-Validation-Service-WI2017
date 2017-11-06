import io.circe._
import io.circe.parser._

object JsonUtils {
  def removeNullValues(json: Json): Json = {
    loadJson(json.pretty(Printer.spaces2.copy(dropNullValues = true)))
  }

  def loadJson(jsonString: String): Json = {
    parse(jsonString).getOrElse(Json.Null)
  }

  def isCorrectJsonString(jsonString: String): Boolean = {
    parse(jsonString) match {
      case Right(json) => true
      case Left(failure) => false
    }
  }
}
