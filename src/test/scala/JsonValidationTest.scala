import org.scalatest.FunSuite

trait JsonValidationTest extends FunSuite {

  val schemaJsonString =
    """
      |{
      |    "$schema": "http://json-schema.org/draft-04/schema#",
      |    "type": "object",
      |    "properties": {
      |      "source": {
      |      "type": "string"
      |    },
      |      "destination": {
      |      "type": "string"
      |    },
      |
      |      "timeout": {
      |      "type": "integer",
      |      "minimum": 0,
      |      "maximum": 32767
      |    },
      |      "chunks": {
      |      "type": "object",
      |      "properties": {
      |      "size": {
      |      "type": "integer"
      |    },
      |      "number": {
      |      "type": "integer"
      |    }
      |    },
      |      "required": ["size"]
      |    }
      |    },
      |    "required": ["source", "destination"]
      |  }
    """.stripMargin
  val schemaJson = JsonUtils.loadJson(schemaJsonString)

  val correctJsonString =
    """
      |{
      |	"source": "/home/alice/image.iso",
      |	"destination": "/mnt/storage",
      |	"chunks": {
      |		"size": 1024
      |	}
      |}
    """.stripMargin
  val correctJson = JsonUtils.loadJson(correctJsonString)

  val incorrectJsonString =
    """
      |{
      |	"chunks": {
      |		"size": 1024
      |	}
      |}
    """.stripMargin
  val incorrectJson = JsonUtils.loadJson(incorrectJsonString)

  test("validate success") {
    assert(JsonValidation.validate(correctJson, schemaJson).isRight)
  }

  test("validate error") {
    assert(JsonValidation.validate(incorrectJson, schemaJson).isLeft)
  }

  test("validate error message") {
    assert(!JsonValidation.validate(incorrectJson, schemaJson).left.get.isEmpty)
  }

}
