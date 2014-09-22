package io.shaka.http

import java.io.File
import java.net.URL
import java.nio.file.Files.readAllBytes

import io.shaka.http.ContentType._
import io.shaka.http.Response._
import io.shaka.http.Status.NOT_FOUND

object StaticResponse {
  type ClasspathDocRoot = Unit => String

  def static(docRoot: String, path: String): Response = new File(s"$docRoot$path") match {
    case dir if dir.isDirectory => respond(dir.listFiles().map(_.getName).mkString("\n")).contentType(TEXT_PLAIN)
    case file if file.exists() => respond(readAllBytes(file.toPath)).contentType(toContentType(path))
    case _ => respond(s"file $path not found").status(NOT_FOUND)
  }

  def static(docRoot: ClasspathDocRoot, path: String): Response = {
    staticFromClasspath(this.getClass.getClassLoader.getResource(docRoot()), path)
  }

  def classpathDocRoot(docRoot: String): ClasspathDocRoot = unit => docRoot

  private def staticFromClasspath(docRoot: URL, path: String): Response = docRoot.getProtocol match {
    case "file" => static(docRoot.getPath, path)
    case "jar" => respond(urlToInputStream(s"${docRoot}$path")).contentType(toContentType(path))
    case protocol => respond(s"Doesn't currently support protocol $protocol")
  }

  def urlToInputStream(url: String) = {
    val is = new URL(url).openStream()
    val bytes = Iterator.continually(is.read()).takeWhile(_ != -1).map(_.toByte).toArray
    is.close()
    bytes
  }

  private val fileExtensionToContentType = Map(
    "css" -> TEXT_CSS,
    "csv" -> TEXT_CSV,
    "htm" -> TEXT_HTML,
    "html" -> TEXT_HTML,
    "xml" -> TEXT_XML,
    "md" -> TEXT_PLAIN,
    "txt" -> TEXT_PLAIN,
    "asc" -> TEXT_PLAIN,
    "gif" -> IMAGE_GIF,
    "jpg" -> IMAGE_JPEG,
    "jpeg" -> IMAGE_JPEG,
    "png" -> IMAGE_PNG,
    "js" -> APPLICATION_JAVASCRIPT,
    "pdf" -> APPLICATION_PDF,
    "exe" -> APPLICATION_OCTET_STREAM
  )

  private def toContentType(path: String) = fileExtensionToContentType.find { case (key, _) => path.endsWith("." + key)}.map(_._2).getOrElse(APPLICATION_OCTET_STREAM)
}
