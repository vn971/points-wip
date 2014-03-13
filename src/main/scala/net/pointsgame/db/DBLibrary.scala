package net.pointsgame.db

import java.sql.Timestamp
import net.pointsgame.ratings.RatingConstants.defaultRating
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Schema}

case class DBUser(
	id: String,
	name: String = "",
	rating: Long = defaultRating,
	ratingPrecision: Double = 1.0
) extends KeyedEntity[String] {
}

case class Game(
	first: String,
	second: String,
	wonFirst: Option[Boolean] = Some(true),
	date: Timestamp = DBLibrary.now()
) {
//	def firstAsDBClass = DBLibrary.gameToFirstUser.right(this)

//	def secondAsDBClass = DBLibrary.gameToSecondUser.right(this)
}

object DBLibrary extends Schema {

	val users = table[DBUser]

	val games = table[Game]

	val messages = table[Message]

	//on(users)(e => declare(
	//		columns(e.id, e.name) are (indexed(""), unique)
	//))

	on(messages)(e => declare(
		e.time is (indexed)
		//e.content is dbType("varchar(300)")
	))

	val gameToFirstUser =
		oneToManyRelation(users, games).
			via((u, g) => u.id === g.first)

	val gameToSecondUser =
		oneToManyRelation(users, games).
			via((u, g) => u.id === g.second)

	def now() = new Timestamp(System.currentTimeMillis)
}
