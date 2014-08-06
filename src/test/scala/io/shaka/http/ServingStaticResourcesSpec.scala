package io.shaka.http

import io.shaka.http.ContentType.{TEXT_CSS, TEXT_HTML}
import io.shaka.http.Http._
import io.shaka.http.HttpServerSpecSupport.withHttpServer
import io.shaka.http.Request.GET
import io.shaka.http.StaticResponse.static
import io.shaka.http.Status.NOT_FOUND
import org.scalatest.Spec

import scala.io.Source._

class ServingStaticResourcesSpec extends Spec{
  val docRoot = "./src/test/scala"
  def `can serve a static file from filesystem`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/io/shaka/http/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/io/shaka/http/test.html").mkString)
    }
  }

  def `return NOT_FOUND when static resource does not exist`(){
    withHttpServer{ (httpServer, rootUrl) =>
      httpServer.handler{
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/io/shaka/http/test2.html")).status === NOT_FOUND)
    }

  }

  def `correctly set content-type when serving static files`() {
    withHttpServer{ (httpServer, rootUrl) =>
      httpServer.handler{
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/io/shaka/http/test.html")).contentType.get === TEXT_HTML)
      assert(http(GET(s"$rootUrl/io/shaka/http/test.css")).contentType.get === TEXT_CSS)
    }
  }

  def `can server a static file from the class path`() {

  }

  def `can server a static file from a jar`() {

  }

  def `shows directory listing when serving static resources`() {
    withHttpServer{ (httpServer, rootUrl) =>
      httpServer.handler{
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/io/shaka/http/testdir"))
      assert(response.entityAsString === List("test.csv","test.txt","testsubdir").mkString("\n"))
    }
  }


}
