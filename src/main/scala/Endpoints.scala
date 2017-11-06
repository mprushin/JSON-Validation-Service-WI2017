import JsonResponseModels.JsonOperationResponse
import io.circe.Json
import io.finch.{Endpoint, Ok, get, post, string, stringBody}

object Endpoints {
  val schemaGet:Endpoint[Json] = get("schema" :: string) {
    (schemaId: String) => {
      JsonStorage.getSchema(schemaId) match {
        case Some(schema) => Ok(schema)
        case None => Ok(JsonUtils.loadJson("No schema with id [%s]".format(schemaId)))
      }
    }
  }

  val schemaPost: Endpoint[JsonOperationResponse] = post("schema" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (!JsonUtils.isCorrectJsonString(jsonString)) {
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
      if (!JsonUtils.isCorrectJsonString(jsonString)) {
        Ok(JsonResponseModels.validateError("Uploaded string isn't a correct JSON"))
      }
      else {
        val jsonSchemaString = JsonStorage.getSchema(schemaId)
        JsonStorage.getSchema(schemaId) match {
          case Some(schemaString) => {
            val json = JsonUtils.loadJson(jsonString)
            val jsonCleaned = JsonUtils.removeNullValues(json)
            val validationResult = JsonValidation.validate(jsonCleaned, schemaString)
            if (validationResult._1) {
              Ok(JsonResponseModels.validatedSuccessfully)
            }
            else {
              Ok(JsonResponseModels.validateError(validationResult._2))
            }
          }
          case None => Ok(JsonResponseModels.validateError("No schema with id [%s] exists.".format(schemaId)))
        }
      }
    }
  }
}
