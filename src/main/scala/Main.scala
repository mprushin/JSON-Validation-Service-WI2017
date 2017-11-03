import JsonResponseModels.JsonOperationResponse
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.param.Stats
import com.twitter.finagle.{Http, Service}
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.circe.Json
import io.circe.generic.auto._
import io.finch._
import io.finch.circe.dropNullValues._


object Main extends TwitterServer {

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

  val api: Service[Request, Response] = (schemaGet :+: schemaPost :+: validatePost).toService

  def main(): Unit = {
    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(":8081", api)

    onExit {
      server.close()
    }

    Await.ready(adminHttpServer)
  }
}
