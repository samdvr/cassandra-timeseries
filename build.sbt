val Http4sVersion = "0.18.14"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.samdvr",
    name := "events",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    )
  )


libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  // Optional for auto-derivation of JSON codecs
  "io.circe" %% "circe-generic" % "0.9.3",
  // Optional for string interpolation to JSON model
  "io.circe" %% "circe-literal" % "0.9.3"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.0"