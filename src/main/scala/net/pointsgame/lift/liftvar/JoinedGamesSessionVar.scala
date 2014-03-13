package net.pointsgame.lift.liftvar

import net.liftweb.http.SessionVar
import net.liftweb.sitemap._
import scala.xml._

case class GameInfo(id: String, nameFirst: String, nameSecond: String)

object JoinedGamesSessionVar extends SessionVar[Seq[GameInfo]](GameInfo("1", "first", "second") :: Nil) {

	def strToMenu(g: GameInfo) = {
		Text(g.nameFirst) ++ Text(" /") ++ <br/> ++ Text(g.nameSecond)
	}

	lazy val htmlMenu: NodeSeq = {
		is.flatMap(strToMenu)
	}

}


