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

  val incorrectJsonString =
    """
      |{
      |	"chunks": {
      |		"size": 1024
      |	}
      |}
    """.stripMargin

  test("validate success") {
    val validation = new JsonValidation()
    assert(validation.validate(correctJsonString, schemaJsonString)._1)
  }

  test("validate error") {
    val validation = new JsonValidation()
    assert(!validation.validate(incorrectJsonString, schemaJsonString)._1)
  }

  test("validate error message") {
    val validation = new JsonValidation()
    assert(!validation.validate(incorrectJsonString, schemaJsonString)._2.isEmpty)
  }

}
