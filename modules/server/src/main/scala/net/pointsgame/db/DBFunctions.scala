// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.db

import net.liftweb.common.Loggable
import net.pointsgame.db.DBLibrary._
import net.pointsgame.db.Pointsgame._

object DBFunctions extends Loggable {

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
