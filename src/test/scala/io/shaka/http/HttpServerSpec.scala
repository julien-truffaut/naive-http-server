package io.shaka.http

import io.shaka.http.ContentType.APPLICATION_JSON
import io.shaka.http.Http.http
import io.shaka.http.HttpHeader.USER_AGENT
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.RequestMatching.&&
import io.shaka.http.Response.respond
import io.shaka.http.Status.NOT_FOUND
import org.scalatest.Spec


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
      httpServer.handler{case req@GET(_) =>
        assert(req.headers.contains(USER_AGENT, userAgent))
        respond("Hello world")
      }
      http(GET(s"http://localhost:${httpServer.port()}").header(USER_AGENT, userAgent))
    }
  }

  def `httpServer sends headers`() {
    withHttpServer{ httpServer =>
      val userAgent = "mytest-agent"
      httpServer.handler{ case GET(_) => respond("Hello world").header(USER_AGENT, userAgent) }
      val response =  http(GET(s"http://localhost:${httpServer.port()}"))
      assert(response.headers.contains(USER_AGENT, userAgent))
    }
  }

  def `match request on content type`() {
    withHttpServer{ httpServer =>
      httpServer.handler{
        case GET(_) && ContentType(APPLICATION_JSON) => respond("""{"hello":"world"}""")
      }
      val response =  http(GET(s"http://localhost:${httpServer.port()}").contentType(APPLICATION_JSON))
      assert(response.status === Status.OK)
    }
  }

  def `can extract path parameters`(){
    withHttpServer{ httpServer =>
      httpServer.handler{
        case GET(Path("tickets" :: ticketId :: "messages" :: messageId :: Nil)) =>
          assert(ticketId === "12")
          assert(messageId === "5")
          respond("""{"hello":"world"}""")
      }
      val response =  http(GET(s"http://localhost:${httpServer.port()}/tickets/12/messages/5"))
      assert(response.status === Status.OK)
    }
  }

  private def withHttpServer(testBlock: HttpServer => Unit){
    val httpServer = HttpServer().start()
    testBlock(httpServer)
    httpServer.stop()
  }

}










    