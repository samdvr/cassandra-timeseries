package com.samdvr.events

import cats.effect.{Effect, IO}
import com.datastax.driver.core.Row
import com.samdvr.events.EventsService.OptionalQueryParamDecoderMatcher
import io.circe.Encoder
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.circe._
import org.http4s.dsl.impl.QueryParamDecoderMatcher

import scala.concurrent.ExecutionContext

object fromTime extends QueryParamDecoderMatcher[String]("from_time")

object toTime extends QueryParamDecoderMatcher[String]("to_time")

object EventsService extends Http4sDsl[IO] {
  implicit val decoder = jsonOf[IO, Event]
  implicit val encoder = jsonEncoderOf[IO, Seq[Event]]

  val repo = new Repository
  val service: HttpService[IO] = {
    HttpService[IO] {

      case GET -> Root / "events" / userID :? fromTime(fromTimestamp) +& toTime(toTimestamp)
      => Ok(repo.fetch(userID, fromTimestamp, toTimestamp))


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