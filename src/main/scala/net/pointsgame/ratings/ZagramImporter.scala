package net.pointsgame.ratings

import java.io.IOException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import net.liftweb.common.Loggable
import net.pointsgame.db.DBLibrary._
import net.pointsgame.db._
import org.squeryl.PrimitiveTypeMode._

object ZagramImporter extends App with Loggable {

	def importGamesWithoutRatings() {
		try {
			def getLinkContent(url: String) =
				try {
					val source = io.Source.fromURL(url, "UTF-8")
					val result = source.getLines().toList
					source.close()
					result
				} catch {
					case e: Exception => Nil
				}

			def getFileContent(fileName: String) =
				try {
					val source = io.Source.fromFile(fileName, "UTF-8")
					val result = source.getLines().toList
					source.close()
					result
				} catch {
					case e: IOException => Nil
				}

			val formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm")

			//	val zagramGamesAsString = getLinkContent("http://zagram.org/games-list2.txt")
			val zagramGames = getFileContent("./src/test/scala/net/pointsgame/zagram-games-protocol2.txt")
			logger info s"exporting approximately ${zagramGames.size} games"
			zagramGames.foreach(handleLine)

			def addPlayer(s: String) {
				try {
					transaction(users.insert(DBUser(s, s)))
				} catch {
					case _: Throwable =>
				}
			}

			def handleLine(l: String) {
				//		logger debug "line: "+l
				val split = l.split('/')
				val first = split(1)
				val second = split(2)
				val dateAsString =
					if (split(0).size <= 10)
						split(0) + "-23-59"
					else
						split(0)
				val date = formatter.parse(dateAsString).getTime
				addPlayer(first)
				addPlayer(second)
				if (split(3) == "1") {
					transaction(games.insert(Game(first, second, Some(true), new Timestamp(date))))
				} else if (split(3) == "2") {
					transaction(games.insert(Game(first, second, Some(false), new Timestamp(date))))
				}
				//				Ratings.refineRating(first, date)
				//				Ratings.refineRating(second, date)
			}
		}
		catch {
			case t: Throwable => logger info t
		}
	}

}
