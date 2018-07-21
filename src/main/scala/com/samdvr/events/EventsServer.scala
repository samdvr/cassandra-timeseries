package com.samdvr.events

import cats.effect.{Effect, IO}
import fs2.StreamApp

import scala.concurrent.ExecutionContext

object EventsServer extends StreamApp[IO] {

  import scala.concurrent.ExecutionContext.Implicits.global
  import org.http4s.server.blaze._


  val builder = BlazeBuilder[IO]
    .bindHttp(8080)
    .mountService(EventsService.service, "/")


  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] =
    builder.serve
}
