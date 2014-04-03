package net.pointsgame

import java.sql.Timestamp
import net.liftweb.common.Loggable
import net.pointsgame.db.Message
import org.joda.time.{DateTimeZone, DateTime}
import org.scalatest._


class MiscTests extends FunSuite with Loggable {

	test("correct Message times") {
		val dateTime = new DateTime(2001, 1, 1, 23, 59, 59, 0).
				withZone(DateTimeZone.forID("Europe/Moscow"))

		assert(
			Message("", "", "", "",
				new Timestamp(dateTime.getMillis)).shortTime == "23:59"
		)

	}

}
