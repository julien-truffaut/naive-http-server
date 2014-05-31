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

    libraryDependencies += "io.shaka" %% "naive-http-server" % "5"

    import io.shaka.http.HttpServer
    val httpServer = HttpServer(request => respond("Hello World!")).start()
    httpServer.stop()

    HttpServer(8080).useHandler(request => repsond("Hello World!)).start()

For more examples see [HttpServerSpec.scala](https://github.com/timt/naive-http-server/blob/master/src/test/scala/io/shaka/http/HttpServerSpec.scala)


Code license
------------
Apache License 2.0