import io.circe.Json
import org.scalatest.FunSuite

trait JsonStorageTest extends FunSuite {
  val schemaString =
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
  val schema = JsonUtils.loadJson(schemaString)
  object JsonTestStorage extends JsonStorage("h2test1")

  test("save and get json") {
    JsonTestStorage.saveSchema(schema, "testSchemaId")
    val schemaJsonLoaded = JsonTestStorage.getSchema("testSchemaId")

    assert(schemaJsonLoaded.getOrElse(Json.Null) == schema)
  }

  test("load non-existent schema") {
    val schemaJsonLoaded = JsonTestStorage.getSchema("nonExistentSchemaId")

    assert(schemaJsonLoaded.isEmpty)
  }

}
