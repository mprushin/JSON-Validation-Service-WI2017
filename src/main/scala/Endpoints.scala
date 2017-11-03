import JsonResponseModels.JsonOperationResponse
import io.circe.Json
import io.finch.{Endpoint, Ok, get, post, string, stringBody}

object Endpoints {
  val schemaGet:Endpoint[Json] = get("schema" :: string) {
    (schemaId: String) => {
      JsonStorage.getSchema(schemaId) match {
        case Some(schemaString) => Ok(JsonUtils.loadJson(schemaString))
        case None => Ok(JsonUtils.loadJson("No schema with id [%s]".format(schemaId)))
      }
    }
  }

  val schemaPost: Endpoint[JsonOperationResponse] = post("schema" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (!JsonUtils.isCorrectJson(jsonString)) {
        Ok(JsonResponseModels.uploadError)
      }
      else {
        JsonStorage.saveSchema(jsonString, schemaId)
        Ok(JsonResponseModels.uploadedSuccessfully)
      }
    }
  }

  val validatePost: Endpoint[JsonOperationResponse] = post("validate" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (!JsonUtils.isCorrectJson(jsonString)) {
        Ok(JsonResponseModels.validateError("Uploaded string isn't a correct JSON"))
      }
      else {
        val jsonSchemaString = JsonStorage.getSchema(schemaId)
        JsonStorage.getSchema(schemaId) match {
          case Some(schemaString) => {
            val jsonStringCleaned = JsonUtils.removeNullValues(jsonString)
            val validationResult = JsonValidation.validate(jsonStringCleaned, schemaString)
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
