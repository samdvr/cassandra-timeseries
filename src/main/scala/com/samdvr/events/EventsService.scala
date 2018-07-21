package com.samdvr.events

import cats.effect.{Effect, IO}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import org.http4s.circe._

import scala.concurrent.ExecutionContext

object EventsService extends Http4sDsl[IO] {
  implicit val decoder = jsonOf[IO, Event]
  val repo = new Repository
  val service: HttpService[IO] = {
    HttpService[IO] {
      case req@POST -> Root / "events" =>
        for {
          // Decode a Event request
          event <- req.as[Event]
          _ = repo.save(event)
          resp <- Created("")
        } yield resp

    }
  }
}