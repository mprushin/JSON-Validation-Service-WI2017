import JsonResponseModels.JsonOperationResponse
import io.circe.Json
import io.finch.{Endpoint, Ok, get, post, string, stringBody}
import io.circe.parser._

object Endpoints {
  val schemaGet: Endpoint[Json] = get("schema" :: string) {
    (schemaId: String) => {
      JsonStorage.getSchema(schemaId) match {
        case Some(schema) => Ok(schema)
        case None => Ok(JsonUtils.loadJson("No schema with id [%s]".format(schemaId)))
      }
    }
  }

  val schemaPost: Endpoint[JsonOperationResponse] = post("schema" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (parse(jsonString).isLeft) {
        Ok(JsonResponseModels.uploadError)
      }
      else {
        JsonStorage.saveSchema(JsonUtils.loadJson(jsonString), schemaId)
        Ok(JsonResponseModels.uploadedSuccessfully)
      }
    }
  }

  val validatePost: Endpoint[JsonOperationResponse] = post("validate" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (parse(jsonString).isLeft) {
        Ok(JsonResponseModels.validateError("Uploaded string isn't a correct JSON"))
      }
      else {
        val jsonSchemaString = JsonStorage.getSchema(schemaId)
        JsonStorage.getSchema(schemaId) match {
          case Some(schema) => {
            val json = JsonUtils.loadJson(jsonString)
            val jsonCleaned = JsonUtils.removeNullValues(json)
              JsonValidation.validate(jsonCleaned, schema) match {
              case Right(flag) => Ok(JsonResponseModels.validatedSuccessfully)
              case Left(message) => Ok(JsonResponseModels.validateError(message))
            }
          }
          case None => Ok(JsonResponseModels.validateError("No schema with id [%s] exists.".format(schemaId)))
        }
      }
    }
  }
}
