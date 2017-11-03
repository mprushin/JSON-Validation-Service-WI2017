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

  val api: Service[Request, Response] = (Endpoints.schemaGet :+: Endpoints.schemaPost :+: Endpoints.validatePost).toService

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
