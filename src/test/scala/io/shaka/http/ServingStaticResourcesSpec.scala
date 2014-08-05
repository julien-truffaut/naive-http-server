package io.shaka.http

import io.shaka.http.Http._
import io.shaka.http.HttpServerSpecSupport.withHttpServer
import io.shaka.http.Request.GET
import io.shaka.http.StaticResponse.static
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

  def `correctly set content-type when serving static files`() {

  }

  def `can server a static file from the class path`() {

  }

  def `shows directory listing when serving static resources`() {

  }


}
