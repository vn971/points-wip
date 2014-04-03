// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.liftvar

import net.liftweb.http.SessionVar
import scala.xml._

case class GameInfo(id: String, nameFirst: String, nameSecond: String)

object JoinedGamesSessionVar extends SessionVar[Seq[GameInfo]](GameInfo("1", "first", "second") :: Nil) {

	private def strToMenu(g: GameInfo) = {
		Text(g.nameFirst) ++ Text(" /") ++ <br/> ++ Text(g.nameSecond)
	}

	lazy val htmlMenu: NodeSeq = {
		is.flatMap(strToMenu)
	}

}


