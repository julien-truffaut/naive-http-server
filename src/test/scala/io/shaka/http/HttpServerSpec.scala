package io.shaka.http

import org.scalatest.Spec
import io.shaka.http.Http.{HttpHandler, http}
import io.shaka.http.Request.{GET, POST}
import scala.Some
import io.shaka.http.HttpHeader.USER_AGENT
import io.shaka.http.Response.respond


class HttpServerSpec extends Spec {

  def `httpServer works`() {
    val handler: HttpHandler = (req) => respond("Hello world")

    val httpServer = HttpServer().useHandler(handler).start()
    val response =  http(GET(s"http://localhost:${httpServer.port()}"))
    httpServer.stop()
    assert(response.status === Status.OK)
    assert(response.entity.get.toString === "Hello world")
  }

  def `httpServer receives GET method`() {
    val handler: HttpHandler = (req) => {
      assert(req.method === Method.GET)
      respond("Hello world")
    }
    val httpServer = HttpServer().useHandler(handler).start()
    http(GET(s"http://localhost:${httpServer.port()}"))
    httpServer.stop()
  }

  def `httpServer receives POST method`() {
    val handler: HttpHandler = (req) => {
      assert(req.method === Method.POST)
      respond("Hello world")
    }
    val httpServer = HttpServer().useHandler(handler).start()
    http(POST(s"http://localhost:${httpServer.port()}"))
    httpServer.stop()
  }

  def `httpServer receives headers`() {
    val userAgent = "mytest-agent"
    val handler: HttpHandler = (req) => {
      assert(req.headers.contains(USER_AGENT))
      assert(req.headers(USER_AGENT).head === userAgent)
      respond("Hello world")
    }
    val httpServer = HttpServer().useHandler(handler).start()
    val response =  http(GET(s"http://localhost:${httpServer.port()}").header(USER_AGENT, userAgent))
    httpServer.stop()
    assert(response.status === Status.OK)
    assert(response.entity.get.toString === "Hello world")
  }

  def `httpServer sends headers`() {
    val userAgent = "mytest-agent"
    val handler: HttpHandler = (req) => {
      respond("Hello world").header(USER_AGENT, userAgent)
    }
    val httpServer = HttpServer().useHandler(handler).start()
    val response =  http(GET(s"http://localhost:${httpServer.port()}"))
    httpServer.stop()
    assert(response.headers.contains(USER_AGENT))
    assert(response.headers(USER_AGENT).head === userAgent)
  }

}








    