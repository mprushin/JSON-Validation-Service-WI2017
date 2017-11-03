import io.circe._
import io.circe.parser._

class JsonClean {
  def removeNullValues(jsonString: String): String = {
    loadJson(jsonString).pretty(Printer.spaces2.copy(dropNullValues = true))
  }

  def loadJson(jsonString: String): Json = {
    parse(jsonString).getOrElse(Json.Null)
  }

  def isCorrectJson(jsonString: String): Boolean = {
    parse(jsonString) match {
      case Right(json) => true
      case Left(failure) => false
    }
  }
}
