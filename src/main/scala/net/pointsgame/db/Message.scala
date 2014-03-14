package net.pointsgame.db

import java.sql.Timestamp
import net.liftweb.common.Loggable
import org.joda.time.{DateTimeZone, DateTime}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations._

case class Message(
	room: String,
	user: String,
	@Column(length = 300) content: String,
	ip: String,
	time: Timestamp = DBLibrary.now()
	) extends Loggable {

	def shortTime = {
		val dt = new DateTime(time.getTime).withZone(DateTimeZone.forID("Europe/Moscow"))
		f"${dt.getHourOfDay}%02d:${dt.getMinuteOfHour}%02d"
	}
}

object MessageFunctions extends Loggable {

	import net.pointsgame.db.DBLibrary.messages

	def loadRoom(room: String) = {
		transaction {
			val sqlQuery = from(messages)(m =>
				where(m.room === room) select (m) orderBy (m.time.desc)).
				page(0, 50)
			Vector(sqlQuery.toSeq: _*).reverse
		}
	}

	def insert(m: Message) {
		try {
			transaction {
				messages.insert(m)
			}
		} catch {
			case e: Exception => logger info "adding message failed"
		}
	}
}
