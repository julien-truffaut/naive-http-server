naive-http-server  [![Build Status](https://travis-ci.org/timt/naive-http-server.png?branch=master)](https://travis-ci.org/timt/naive-http-server) [ ![Download](https://api.bintray.com/packages/timt/repo/naive-http-server/images/download.png) ](https://bintray.com/timt/repo/naive-http-server/_latestVersion)
=================
A really simple http server library implemented in scala with no dependencies

Requirements
------------

* [scala](http://www.scala-lang.org) 2.10.4
* [scala](http://www.scala-lang.org) 2.11.0

Usage
-----
Add the following lines to your build.sbt

    resolvers += "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"

    libraryDependencies += "io.shaka" %% "naive-http-server" % "18"

Starting a server

    import io.shaka.http.HttpServer
    import io.shaka.http.Response.respond
    val httpServer = HttpServer(request => respond("Hello World!")).start()
    ...
    val httpServer = HttpServer(8080).handler(request => respond("Hello World!")).start()

Handling requests

    import io.shaka.http.RequestMatching._
    httpServer.handler{
        case GET("/hello") => respond("Hello world")
        case GET(echoUrl) => respond(echoUrl)
        case request@POST("/some/restful/thing") => respond(...)
        case GET(_) && Accept(APPLICATION_JSON) => respond("""{"hello":"world"}""").contentType(APPLICATION_JSON)
        case GET(url"/tickets/$ticketId?messageContains=$messageContains") => respond(s"Ticket $ticketId, messageContains $messageContains").contentType(TEXT_PLAIN)
        case _ => respond("doh!").status(NOT_FOUND)
    }
    
Serving static content
    
    import io.shaka.http.StaticResponse.static
    httpServer.handler{
        case GET(path) => static("/home/timt/docRoot", path)
    }


Stopping the server

    httpServer.stop()


For more examples see [HttpServerSpec.scala](https://github.com/timt/naive-http-server/blob/master/src/test/scala/io/shaka/http/HttpServerSpec.scala)


Code license
------------
Apache License 2.0
