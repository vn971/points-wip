package net.pointsgame.db

import java.sql.Timestamp
import net.pointsgame.db.PointsgameSqlTypes._
import org.joda.time.{DateTimeZone, DateTime}
import org.squeryl.annotations._

object Message {
	final val CHARACTER_LIMIT = 300
}

case class Message(
	room: String,
	user: String,
	@Column(length = Message.CHARACTER_LIMIT) content: String,
	ip: String,
	time: Timestamp = DBLibrary.now()
	) {

	def shortTime = {
		val dt = new DateTime(time.getTime).withZone(DateTimeZone.forID("Europe/Moscow"))
		f"${dt.getHourOfDay}%02d:${dt.getMinuteOfHour}%02d"
	}
}

object MessageFunctions {
	final val KEEP_COUNT = 50

	import net.pointsgame.db.DBLibrary.messages

	def loadRoom(room: String) = {
		transaction {
			val sqlQuery = from(messages)(m =>
				where(m.room === room) select (m) orderBy (m.time.desc)).
				page(0, KEEP_COUNT)
			Vector(sqlQuery.toSeq: _*).reverse
		}
	}

	def insert(m: Message): Unit = {
		try {
			transaction {
				messages.insert(m)
			}
		} catch {
			case e: Exception =>
//				logger.info(s"adding message $m failed")
		}
	}
}
