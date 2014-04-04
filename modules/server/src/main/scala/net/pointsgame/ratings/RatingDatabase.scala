// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.ratings

import net.pointsgame.db.DBLibrary._
import net.pointsgame.db.DBUser
import net.pointsgame.db.Pointsgame._
import scala.Some

trait RatingDatabaseComponent {
	def ratingDatabase: RatingDatabase

	trait RatingDatabase {
		def getAllUsers: List[DBUser]

		def saveUser(userId: Long, newRating: Long, precision: Double): Unit

		def gamesByUser(user: Long): List[RatingGameInfo]
	}

}


class H2RatingDatabase extends RatingDatabaseComponent {
	val ratingDatabase = new RatingDatabase {
		override def getAllUsers: List[DBUser] = transaction {
			users.allRows.toList
		}


		override def saveUser(userId: Long, newRating: Long, precision: Double): Unit = {
			transaction {
				users.update(u =>
					where(u.id === userId)
							set(u.rating := newRating, u.ratingPrecision := 1.0)
				)
			}
		}

		override def gamesByUser(user: Long): List[RatingGameInfo] = {
			val gamesList = from(games)(game =>
				where(game.firstId === user or game.secondId === user)
						select (game)
			).toList
			???
		}

	}
}
