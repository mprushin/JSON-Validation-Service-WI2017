import io.circe._
import io.circe.parser._

object JsonUtils {
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

  def prettifyJson(jsonString: String): String ={
    loadJson(jsonString).pretty(Printer.spaces2)
  }
}
