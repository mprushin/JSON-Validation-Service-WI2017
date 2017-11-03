import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.param.Stats
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.finch._

object Main extends TwitterServer  {

  val schemaGet: Endpoint[String] = get("schema" :: string) {
    (schemaId: String) => Ok("SCHEMA GET - " + schemaId)
  }

  val schemaPost: Endpoint[String] = post("schema" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => Ok("SCHEMA POST - " + schemaId)
  }

  val validatePost: Endpoint[String] = post("validate" :: string :: stringBody) {
    (schemaId: String, jsonString: String) => Ok("Validate Post - " + schemaId)
  }

  val api: Service[Request, Response] = (schemaGet :+: schemaPost :+: validatePost).toServiceAs[Text.Plain]

  def main(): Unit = {
    val server = Http.server
      .configured(Stats(statsReceiver))
      .serve(":8081", api)

    onExit { server.close() }

    Await.ready(adminHttpServer)
  }
}
