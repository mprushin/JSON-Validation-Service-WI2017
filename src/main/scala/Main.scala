import JsonResponseModels.JsonOperationResponse
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.param.Stats
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.finch._
import io.circe.generic.auto._
import io.finch.circe.dropNullValues._


object Main extends TwitterServer {

  val schemaGet: Endpoint[String] = get("schema" :: string) {
    (schemaId: String) => {
      val storage = new JsonStorage
      storage.getSchema(schemaId) match {
        case Some(schemaString) => Ok(schemaString)
        case None => Ok("No schema with id [%s]".format(schemaId))
      }
    }
  }

  val schemaPost: Endpoint[JsonOperationResponse] = post("schema" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      val jsonClean = new JsonClean
      if (!jsonClean.isCorrectJson(jsonString)) {
        Ok(JsonResponseModels.uploadError)
      }
      else {
        val storage = new JsonStorage
        storage.saveSchema(jsonString, schemaId)
        Ok(JsonResponseModels.uploadedSuccessfully)
      }
    }
  }

  val validatePost: Endpoint[JsonOperationResponse] = post("validate" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => {
      val jsonClean = new JsonClean
      if (!jsonClean.isCorrectJson(jsonString)) {
        Ok(JsonResponseModels.validateError("Uploaded string isn't a correct JSON"))
      }
      else {
        val jsonStorage = new JsonStorage
        val jsonSchemaString = jsonStorage.getSchema(schemaId)
        jsonStorage.getSchema(schemaId) match {
          case Some(schemaString) => {
            val jsonValidation = new JsonValidation
            val jsonStringCleaned = jsonClean.removeNullValues(jsonString)
            val validationResult = jsonValidation.validate(jsonStringCleaned, schemaString)
            if (validationResult._1) {
              Ok(JsonResponseModels.validatedSuccessfully)
            }
            else {
              Ok(JsonResponseModels.validateError(validationResult._2))
            }
          }
          case None => Ok(JsonResponseModels.validateError("No schema with id [%s]".format(schemaId)))
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
