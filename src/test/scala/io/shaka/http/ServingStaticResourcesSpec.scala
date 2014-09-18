package io.shaka.http

import io.shaka.http.ContentType.{TEXT_CSS, TEXT_CSV, TEXT_HTML}
import io.shaka.http.Http._
import io.shaka.http.HttpServerSpecSupport.withHttpServer
import io.shaka.http.Request.GET
import io.shaka.http.StaticResponse.{classpathDocRoot, static}
import io.shaka.http.Status.NOT_FOUND
import org.scalatest.Spec

import scala.io.Source._

class ServingStaticResourcesSpec extends Spec {
  val docRoot = "./src/test/resources/web"

  def `can serve a static file from filesystem`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/test.html").mkString)
    }
  }

  def `return NOT_FOUND when static resource does not exist`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/test2.html")).status === NOT_FOUND)
    }

  }

  def `correctly set content-type when serving static files`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      assert(http(GET(s"$rootUrl/test.html")).contentType.get === TEXT_HTML)
      assert(http(GET(s"$rootUrl/test.css")).contentType.get === TEXT_CSS)
      assert(http(GET(s"$rootUrl/testdir/test.csv")).contentType.get === TEXT_CSV)
    }
  }

  def `shows directory listing when serving static resources`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(docRoot, path)
      }
      val response = http(GET(s"$rootUrl/testdir"))
      assert(response.entityAsString.split("\n").sorted === List("test.csv", "testsubdir", "test.txt").sorted)
    }
  }


  def `can server a static file from the (file:) classpath`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("web"), path)
      }
      val response = http(GET(s"$rootUrl/test.html"))
      assert(response.entityAsString === fromFile(s"$docRoot/test.html").mkString)
    }
  }

  def `shows directory listing when serving static resources from (file:) classpath`() {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(path) => static(classpathDocRoot("web"), path)
      }
      val response = http(GET(s"$rootUrl/testdir"))
      assert(response.entityAsString.split("\n").sorted === List("test.csv", "testsubdir", "test.txt").sorted)
    }
  }

  def `can server a static file from a jar`() {

  }

}
