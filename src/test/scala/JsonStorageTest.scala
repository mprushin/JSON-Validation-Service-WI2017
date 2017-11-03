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


  test("save and get json") {
    val storage = new JsonStorage
    storage.saveSchema(schemaString, "testSchemaId")
    val schemaJsonStringLoaded = storage.getSchema("testSchemaId")

    assert(schemaJsonStringLoaded.getOrElse("") == schemaString)
  }

  test("load non-existent schema") {
    val storage = new JsonStorage
    val schemaJsonStringLoaded = storage.getSchema("nonExistentSchemaId")

    assert(schemaJsonStringLoaded.isEmpty)
  }

}
