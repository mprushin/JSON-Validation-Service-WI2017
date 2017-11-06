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

  test("save and get json") {
    JsonStorage.saveSchema(schema, "testSchemaId")
    val schemaJsonLoaded = JsonStorage.getSchema("testSchemaId")

    assert(schemaJsonLoaded.getOrElse(Json.Null) == schema)
  }

  test("load non-existent schema") {
    val schemaJsonLoaded = JsonStorage.getSchema("nonExistentSchemaId")

    assert(schemaJsonLoaded.isEmpty)
  }

}
