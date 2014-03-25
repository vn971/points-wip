package net.pointsgame.ratings

import java.text.SimpleDateFormat
import net.liftweb.common.Loggable
import net.pointsgame.helpers.BlockingIO

object ZagramImporter extends Loggable {

	@deprecated("to be refactored", "0.0")
	def importZagramGames() = {
		// http://zagram.org/games-list2.txt
		val zagramGameLines = BlockingIO.getFileAsLines("./src/test/scala/net/pointsgame/zagram-games-protocol2.txt").get
		logger.info(s"importing approximately ${zagramGameLines.length} games")

		val formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm")
		def parseLine(l: String) = {
			//		logger debug "line: "+l
			val split = l.split('/')
			val first = split(1)
			val second = split(2)
			val dateAsString =
				if (split(0).size <= 10) {
					split(0) + "-23-59"
				} else {
					split(0)
				}
			val date = formatter.parse(dateAsString).getTime
			val whoWon = split(3) match {
				case "1" => FirstWon
				case "2" => SecondWon
				case _ => Draw
			}
			Tuple4(first, second, whoWon, date)
		}

		zagramGameLines.map(parseLine)
	}

}
