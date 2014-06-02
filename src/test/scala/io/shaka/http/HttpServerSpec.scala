package io.shaka.http

import org.scalatest.Spec
import io.shaka.http.Http.http
import io.shaka.http.HttpHeader.USER_AGENT
import io.shaka.http.Response.respond
import io.shaka.http.Request.{POST, GET}
import io.shaka.http.Status.NOT_FOUND


class HttpServerSpec extends Spec {

  def `httpServer works`() {
    withHttpServer { httpServer =>
        httpServer.handler(req => respond("Hello world"))
        val response = http(GET(s"http://localhost:${httpServer.port()}"))
        assert(response.status === Status.OK)
        assert(response.entity.get.toString === "Hello world")
    }
  }

  def `httpServer receives GET method`() {
    withHttpServer{ httpServer =>
      httpServer.handler{
        case req@GET(_) => respond("Hello world")
        case _ => respond("doh!").status(NOT_FOUND)
      }
      http(GET(s"http://localhost:${httpServer.port()}"))
    }
  }

  def `httpServer receives POST method`() {
    withHttpServer{ httpServer =>
      httpServer.handler{ case POST(_) => respond("Hello world") }
      http(POST(s"http://localhost:${httpServer.port()}"))
    }
  }

  def `httpServer receives headers`() {
    withHttpServer{ httpServer =>
      val userAgent = "mytest-agent"
      httpServer.handler{case req@GET(_) => {
        assert(req.headers.contains(USER_AGENT))
        assert(req.headers(USER_AGENT).head === userAgent)
        respond("Hello world")
      }}
      http(GET(s"http://localhost:${httpServer.port()}").header(USER_AGENT, userAgent))
    }
  }

  def `httpServer sends headers`() {
    withHttpServer{ httpServer =>
      val userAgent = "mytest-agent"
      httpServer.handler{ case GET(_) => respond("Hello world").header(USER_AGENT, userAgent) }
      val response =  http(GET(s"http://localhost:${httpServer.port()}"))
      assert(response.headers.contains(USER_AGENT))
      assert(response.headers(USER_AGENT).head === userAgent)
    }
  }

  private def withHttpServer(testBlock: HttpServer => Unit){
    val httpServer = HttpServer().start()
    testBlock(httpServer)
    httpServer.stop()
  }

}









    