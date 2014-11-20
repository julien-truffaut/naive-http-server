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

  object Accept {
    def unapply(request: Request) = if (request.headers.contains(ACCEPT)) request.headers(ACCEPT).headOption.map(ContentType.contentType) else None
  }

  object Path {
    def unapply(path: String): Option[List[String]] = Some(path.split("/").toList match {
      case "" :: xs => xs
      case x => x
    })
  }

}
