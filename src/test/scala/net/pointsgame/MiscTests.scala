package net.pointsgame

import java.sql.Timestamp
import net.liftweb.common.Loggable
import net.pointsgame.db.Message
import org.joda.time.{DateTimeZone, DateTime}
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers


class MiscTests extends FunSuite with Loggable with ShouldMatchers {

	test("correct Message times") {
		val dateTime = new DateTime(2001, 1, 1, 23, 59, 59, 0).
			withZone(DateTimeZone.forID("Europe/Moscow"))

		Message("", "", "", "", new Timestamp(dateTime.getMillis)).
			shortTime should be === "23:59"

		Message("", "", "", "", new Timestamp(2001, 1, 1, 23, 59, 59, 0)).
			shortTime should be === "23:59"
	}

}
