package net.pointsgame.db

import java.sql.Timestamp
import net.pointsgame.db.Pointsgame._
import net.pointsgame.ratings.RatingConstants.defaultRating
import net.pointsgame.ratings.RatingUserInfo
import org.squeryl.{KeyedEntity, Schema}


case class DBUser(
		id: Long = 0L,
		name: String,
		rating: Long = defaultRating,
		ratingPrecision: Double = 1.0
		)
		extends KeyedEntity[Long] {
	val lowerCase: String = name.toLowerCase
}

case class DBGame(
		id: Long = 0L,
		firstId: Long,
		secondId: Long,
		wonFirst: Option[Boolean] = Some(true),
		date: Timestamp = DBLibrary.now()
		// gameStarted, gameEnded
		// userFirstMoves
		// userWhoInvited
		) extends KeyedEntity[Long] {
	lazy val first = DBLibrary.gameToFirstUser.rightStateful(this)
	lazy val second = DBLibrary.gameToSecondUser.rightStateful(this)
	lazy val firstStateless = DBLibrary.gameToFirstUser.right(this)
	lazy val secondStateless = DBLibrary.gameToSecondUser.right(this)
}

object DBLibrary extends Schema {

	val users = table[DBUser]

	val games = table[DBGame]

	val messages = table[Message]

	on(users)(e => declare(
		e.lowerCase is unique,
		e.name is(indexed, unique)
	))

	on(messages)(e => declare(
		e.time is (indexed)
	))

	val gameToFirstUser =
		oneToManyRelation(users, games).
				via((u, g) => u.id === g.firstId)

	val gameToSecondUser =
		oneToManyRelation(users, games).
				via((u, g) => u.id === g.secondId)

	def now() = new Timestamp(System.currentTimeMillis)
}
