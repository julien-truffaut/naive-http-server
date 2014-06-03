package io.shaka.http

object RequestMatching {
  object && {
    def unapply[A](a: A) = Some((a, a))
  }
}
