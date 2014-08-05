package io.shaka.http

import io.shaka.http.ContentType._
import io.shaka.http.Response._

import scala.io.Source._

object StaticResponse{
  def static(docRoot: String, path: String) = respond(path)
    .entity(fromFile(s"$docRoot$path").mkString)
    .contentType(TEXT_HTML)

  private val fileExtensionToContentType = Map(
    "css"-> TEXT_CSS,
    "htm"-> TEXT_HTML,
    "html"-> TEXT_HTML,
    "xml"-> TEXT_XML,
    "md"-> TEXT_PLAIN,
    "txt"-> TEXT_PLAIN,
    "asc"-> TEXT_PLAIN,
    "gif"-> IMAGE_GIF,
    "jpg"-> IMAGE_JPEG,
    "jpeg"-> IMAGE_JPEG,
    "png"-> IMAGE_PNG,
    "js"-> APPLICATION_JAVASCRIPT,
    "pdf"-> APPLICATION_PDF,
    "exe"-> APPLICATION_OCTET_STREAM
  )

  private def toContentType(path: String) = fileExtensionToContentType.find{ case (key, _) => path.endsWith("."+key)}.map(_._2).getOrElse(APPLICATION_OCTET_STREAM)
}
