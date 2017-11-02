import io.circe.Printer
import org.scalatest.FunSuite

trait JsonCleanTest extends FunSuite {
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

  test("remove nulls") {
    val jsonClean = new JsonClean
    val jsonStringWithoutNullsPretty = jsonClean.loadJson(jsonStringWithoutNulls).pretty(Printer.spaces2)
    assert(jsonClean.removeNullValues(jsonStringWithNulls) == jsonStringWithoutNullsPretty)
  }
}
