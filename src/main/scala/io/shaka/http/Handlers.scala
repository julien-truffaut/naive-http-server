package io.shaka.http

import io.shaka.http.Http.HttpHandler
import io.shaka.http.HttpHeader.CONTENT_LENGTH
import io.shaka.http.Method.{GET, HEAD}
import io.shaka.http.Status.INTERNAL_SERVER_ERROR

object Handlers {

  object HEADRequestHandler {
    def ~>(handler: HttpHandler): HttpHandler = (request) => {
      val isHeadRequest = request.method == HEAD
      val response = handler(if (isHeadRequest) request.copy(method = GET) else request)
      if(isHeadRequest) response.header(CONTENT_LENGTH, response.entity.fold("0")(_.content.length.toString)) else response
    }
  }

  object SafeRequestHandler {
    def ~>(handler: HttpHandler): HttpHandler = (request) => try {
      handler(request)
    } catch {
      case e: Throwable => Response().entity(s"Server error: ${e.getMessage}").status(INTERNAL_SERVER_ERROR)
    }
  }

}
