name := "naive-http-server"

organization := "io.shaka"

version := "1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

unmanagedSourceDirectories in Compile += baseDirectory.value / "naive-http/src/main/scala"