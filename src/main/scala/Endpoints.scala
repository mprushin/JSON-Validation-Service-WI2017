import JsonResponseModels.JsonOperationResponse
import io.circe.generic.auto._
import io.circe._
import io.finch._
import io.circe.parser._
import io.circe.syntax._

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
        //BadRequest(new Exception(JsonResponseModels.uploadError.asJson.pretty(Printer.spaces2)))
      }
      else {
        JsonStorage.saveSchema(JsonUtils.loadJson(jsonString), schemaId)
        Created(JsonResponseModels.uploadedSuccessfully)
      }
    }
  }

  val validatePost: Endpoint[JsonOperationResponse] = post("validate" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      if (parse(jsonString).isLeft) {
        Ok(JsonResponseModels.validateError("Uploaded string isn't a correct JSON"))
        //BadRequest(new Exception(JsonResponseModels.validateError("Uploaded string isn't a correct JSON").asJson.pretty(Printer.spaces2)))
      }
      else {
        val jsonSchemaString = JsonStorage.getSchema(schemaId)
        JsonStorage.getSchema(schemaId) match {
          case Some(schema) => {
            val json = JsonUtils.loadJson(jsonString)
            val jsonCleaned = JsonUtils.removeNullValues(json)
            JsonValidation.validate(jsonCleaned, schema) match {
              case Right(json) => Ok(JsonResponseModels.validatedSuccessfully)
              case Left(message) => {
                Ok(JsonResponseModels.validateError(message))
                //BadRequest(new Exception(JsonResponseModels.validateError(message).asJson.pretty(Printer.spaces2)))
              }
            }
          }
          case None => {
            Ok(JsonResponseModels.validateError("No schema with id [%s] exists.".format(schemaId)))
            //BadRequest(new Exception(JsonResponseModels.validateError("No schema with id [%s] exists.".format(schemaId)).asJson.pretty(Printer.spaces2)))
          }
        }
      }
    }
  }
}
