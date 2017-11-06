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
  val jsonWithNulls = JsonUtils.loadJson(jsonStringWithNulls)

  val jsonStringWithoutNulls =
    """{
      |	"source" : "/home/alice/image.iso",
      |	"destination" : "/mnt/storage",
      |	"chunks" : {
      |	"size" : 1024
      |	}
      |}
    """.stripMargin
  val jsonWithoutNulls = JsonUtils.loadJson(jsonStringWithoutNulls)

  val notAJson = "dasdsadsadas"

  test("remove nulls") {
    assert(JsonUtils.removeNullValues(jsonWithNulls) == jsonWithoutNulls)
  }

  test("isCorrectJson true"){
    assert(JsonUtils.isCorrectJsonString(jsonStringWithoutNulls))
  }

  test("isCorrectJson false"){
    assert(!JsonUtils.isCorrectJsonString(notAJson))
  }
}
