package com.samdvr.events

import java.sql.Timestamp
import java.util.UUID


import com.datastax.driver.core._

import scala.concurrent.ExecutionContext

case class Event(id: UUID, userID: UUID, timestamp: String, body: String)

import scala.collection.JavaConverters._
class Repository {
  implicit val session = Cluster.builder
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .build
    .connect()


  def save(event: Event) = {
    session.executeAsync(s"INSERT INTO events.Events(user_id, timestamp, body, event_id) " +
      s"VALUES (?,?,?,?)",
      event.userID, Timestamp.valueOf(event.timestamp), event.body, event.id)
  }

  def fetch(userID: String, from: String, to: String): Seq[Event] = {
    session.execute(s"SELECT * FROM events.Events WHERE user_id = ? " +
      s"AND timestamp > ? AND timestamp < ?", UUID.fromString(userID), Timestamp.valueOf(from), Timestamp.valueOf(to)).all.asScala.map { row =>
      Event(row.getUUID("user_id"),
        row.getUUID("event_id"),
        row.getTimestamp("timestamp").toString,
        row.getString("body")
      )
    }


  }
}
