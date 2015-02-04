package io.shaka.http

import io.shaka.http.HttpHeader.ACCEPT

object RequestMatching {
  object && {
    def unapply[A](a: A) = Some((a, a))
  }

  implicit class URLMatcher(val sc: StringContext) extends AnyVal {
    def url = sc.parts.mkString("(.+)")
      .replaceAllLiterally("?", "\\?")
      .r
  }

  @deprecated("Only works for exactly 1 accept header value. Instead import RequestOps and use guard condition 'if req.accepts(someContentType)'", "2015-02-04")
  object Accept {
    def unapply(request: Request) = if (request.headers.contains(ACCEPT)) request.headers(ACCEPT).headOption.map(ContentType.contentType) else None
  }

  object Path {
    def unapply(path: String): Option[List[String]] = Some(path.split("/").toList match {
      case "" :: xs => xs
      case x => x
    })
  }

  implicit class RequestOps(request: Request) {
    def accepts(contentType: ContentType): Boolean =
      request.headers(ACCEPT).exists(_.contains(contentType.value))
  }

}
