package io.shaka.http

import org.scalatest.Spec
import scala.Some
import io.shaka.http.Http.HttpHandler
import io.shaka.http.Http.http
import io.shaka.http.Request.GET


class HttpServerSpec extends Spec {

  def `httpServer works`() {
    val handler: HttpHandler = (req) => StringResponse("Hello world")

    val httpServer = HttpServer().useHandler(handler).start()
    val response =  http(GET(s"http://localhost:${httpServer.port()}"))
    httpServer.stop()
    assert(response.status === Status.OK)
    assert(response.entity.get.toString === "Hello world")
  }

  def StringResponse(value: String) = Response(entity = Some(Entity(value)))

}








    