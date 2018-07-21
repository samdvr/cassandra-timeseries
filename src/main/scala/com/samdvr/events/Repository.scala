package com.samdvr.events

import java.sql.Timestamp
import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import akka.stream.scaladsl.{Keep, Source}
import com.datastax.driver.core.{Cluster, PreparedStatement}

import scala.concurrent.ExecutionContext

case class Event(id: UUID, userID: UUID, timestamp: String, body: String)


class Repository {
  implicit val system: ActorSystem = ActorSystem("system")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val session = Cluster.builder
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .build
    .connect()
  val preparedStatement = session.prepare(s"INSERT INTO events.Events(user_id, timestamp, body, event_id) VALUES (?, ?,?,?)")

  def save(event: Event) = {
    val source: Source[Event, NotUsed] = Source.single(event)
    val binder = (event: Event, statement: PreparedStatement) => statement.bind(event.userID, Timestamp.valueOf(event.timestamp), event.body, event.id)

    val sink = CassandraSink[Event](parallelism = 2, preparedStatement, binder)
    source.toMat(sink)(Keep.right)
      .run()


  }
}
