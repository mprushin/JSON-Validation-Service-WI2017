import io.finch.circe._
import io.finch.{Application, Input}
import org.scalatest.FunSuite

trait EndpointsTest extends FunSuite {
  val schemaStringEndpoints =
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

  val jsonStringEndpoints = {
    """{
      |"source": "/home/alice/image.iso",
      |"destination": "/mnt/storage",
      |"timeout": null,
      |"chunks": {
      |"size": 1024,
      |"number": null
      |}
      |}""".stripMargin
  }

  val jsonStringEndpointsInvalid = {
    """{
      |"timeout": null,
      |"chunks": {
      |"size": 1024,
      |"number": null
      |}
      |}""".stripMargin
  }

  test("post and get schema") {
    val postRequest = Input.post("/schema/endpointTest").withBody[Application.Json](JsonUtils.loadJson(schemaStringEndpoints))
    assert(Endpoints.schemaPost(postRequest).awaitValueUnsafe().getOrElse(null) == JsonResponseModels.uploadedSuccessfully)

    val getRequest = Input.get("/schema/endpointTest")
    assert(Endpoints.schemaGet(getRequest).awaitValueUnsafe().getOrElse(null) == JsonUtils.loadJson(schemaStringEndpoints))
  }

  test("post schema and verify successfully") {
    val postRequest = Input.post("/schema/endpointTest2").withBody[Application.Json](JsonUtils.loadJson(schemaStringEndpoints))
    assert(Endpoints.schemaPost(postRequest).awaitValueUnsafe().getOrElse(null) == JsonResponseModels.uploadedSuccessfully)

    val verifyRequest = Input.post("/validate/endpointTest2").withBody[Application.Json](JsonUtils.loadJson(jsonStringEndpoints))
    assert(Endpoints.validatePost(verifyRequest).awaitValueUnsafe().getOrElse(null) == JsonResponseModels.validatedSuccessfully)
  }

  test("post schema and verify unsuccessfully") {
    val postRequest = Input.post("/schema/endpointTest2").withBody[Application.Json](JsonUtils.loadJson(schemaStringEndpoints))
    assert(Endpoints.schemaPost(postRequest).awaitValueUnsafe().getOrElse(null) == JsonResponseModels.uploadedSuccessfully)

    val verifyRequest = Input.post("/validate/endpointTest2").withBody[Application.Json](JsonUtils.loadJson(jsonStringEndpointsInvalid))
    assert(Endpoints.validatePost(verifyRequest).awaitValueUnsafe().getOrElse(null) == JsonResponseModels.validateError("object has missing required properties ([\"destination\",\"source\"])"))
  }

}
