package io.shaka.http

object HttpServerSpecSupport {
  def withHttpServer(testBlock: HttpServer => Unit) {
    val httpServer = HttpServer().start()
    testBlock(httpServer)
    httpServer.stop()
  }
}
