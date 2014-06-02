package io.shaka.http

import java.net.InetSocketAddress
import com.sun.net.httpserver.{HttpServer => SunHttpServer}
import io.shaka.http.Http.HttpHandler
import Response.respond
import Status.NOT_FOUND


class HttpServer(private val usePort:Int = 0) {

  val server =  SunHttpServer.create(new InetSocketAddress(usePort), 0)
  server.setExecutor(null)
  server.createContext("/", new SunHttpHandlerAdapter((req) => respond("No handler defined!").status(NOT_FOUND)))

  def start() = {
    val startedAt = System.nanoTime()
    server.start()
    val elapsedTime = BigDecimal((System.nanoTime() - startedAt) / 1000000.0).formatted("%.2f")
    println(s"naive-http-server started on port ${port()} in $elapsedTime milli seconds")
    this
  }

  def stop() {
    val delayInSeconds = 0
    server.stop(delayInSeconds)
  }

  def port() = server.getAddress.getPort

  def handler(handler: HttpHandler) = {
    server.removeContext("/")
    server.createContext("/", new SunHttpHandlerAdapter(handler))
    this
  }

}

object HttpServer {
  def apply(): HttpServer = apply(0)
  def apply(port: Int): HttpServer = new HttpServer(port)
  def apply(handler: HttpHandler, port: Int = 0): HttpServer = apply(port).handler(handler)
}