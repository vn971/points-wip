package net.pointsgame.db

import net.pointsgame.db.DBLibrary._
import net.pointsgame.db.PointsgameSqlTypes._

object DBFunctions {

	def findGames(user: Long): List[DBGame] =
		transaction {
			DBLibrary.games.where(g => g.firstId === user or g.secondId === user).toList
		}

	def getRating(userId: Long): Long = {
		transaction {
			users.lookup(userId).map(_.rating).getOrElse(-1L)
		}
	}

}
