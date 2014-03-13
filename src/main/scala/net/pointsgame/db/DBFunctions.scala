package net.pointsgame.db

import net.liftweb.common.Loggable
import net.pointsgame.db.DBLibrary._
import org.squeryl.PrimitiveTypeMode._

object DBFunctions extends Loggable {

	def findGames(user: String) =
		transaction {
			DBLibrary.games.where(g => g.first === user or g.second === user).toList
		}

	def getRating(user: String) = {
		transaction {
			users.lookup(user).map(_.rating).getOrElse(-1L)
		}
	}

}
