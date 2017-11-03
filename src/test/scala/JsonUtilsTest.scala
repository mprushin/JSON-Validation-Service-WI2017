import io.circe.Printer
import org.scalatest.FunSuite

trait JsonUtilsTest extends FunSuite {
  val jsonStringWithNulls =
    """
      |{
      |	"source" : "/home/alice/image.iso",
      |	"destination" : "/mnt/storage",
      | "timeout" : null,
      |	"chunks" : {
      |		"size" : 1024,
      | 	"number" : null
      |	}
      |}
    """.stripMargin

  val jsonStringWithoutNulls =
    """{
      |	"source" : "/home/alice/image.iso",
      |	"destination" : "/mnt/storage",
      |	"chunks" : {
      |	"size" : 1024
      |	}
      |}
    """.stripMargin

  val notAJson = "dasdsadsadas"

  test("remove nulls") {
    val jsonStringWithoutNullsPretty = JsonUtils.loadJson(jsonStringWithoutNulls).pretty(Printer.spaces2)
    assert(JsonUtils.removeNullValues(jsonStringWithNulls) == jsonStringWithoutNullsPretty)
  }

  test("isCorrectJson true"){
    assert(JsonUtils.isCorrectJson(jsonStringWithoutNulls))
  }

  test("isCorrectJson false"){
    assert(!JsonUtils.isCorrectJson(notAJson))
  }
}
