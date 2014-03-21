package net.pointsgame.ai

import akka.actor._
import net.pointsgame.lift.comet.PaperSingleton
import net.pointsgame.lift.model._
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface
import scala.collection.JavaConversions._

class DuelWithEngine extends Actor with ActorLogging {

	var surroundingsNumber = 0 // do not recalculate surroundings each time...
	val engine = new SingleGameEngine(32, 32)

	private def javaSurroundingToScalaSurrounding(s: SingleGameEngineInterface.SurroundingAbstract) = {
		Surrounding(s.isRed, s.path.toList.map(d => Dot(d.x - 1, d.y - 1)))
	}

	def handleDot(x: Int,y: Int, red_? : Boolean): Unit = {
			if (engine.canMakeMove(x + 1, y + 1)) {
				engine.makeMove(x + 1, y + 1, red_?)
				context.become(if (red_?) bluesTurn else redsTurn, discardOld = true)

				PaperSingleton ! DotColored(red_?, x, y)

				{
					def sameColor(x: Int, y: Int) = if (red_?) engine.isRed(x, y) else engine.isBlue(x, y)
					if (sameColor(x, y + 1)) PaperSingleton ! (RightConnection(red_?, x - 1, y): PaperEvent)
					if (sameColor(x + 1, y)) PaperSingleton ! (BottomConnection(red_?, x, y - 1): PaperEvent)
					if (sameColor(x + 2, y + 1)) PaperSingleton ! (RightConnection(red_?, x, y): PaperEvent)
					if (sameColor(x + 1, y + 2)) PaperSingleton ! (BottomConnection(red_?, x, y): PaperEvent)
				}

				for (surr <- engine.getLastMoveInfo.newSurroundings if !surr.isCtrl) {
					PaperSingleton ! javaSurroundingToScalaSurrounding(surr)
				}
			}
	}

	def redsTurn: Receive = {
		case Dot(x, y) => handleDot(x, y, red_? = true)
	}

	def bluesTurn: Receive = {
		case Dot(x, y) => handleDot(x, y, red_? = false)
	}

	override def receive = bluesTurn

	override def preStart() {
		log.info(s"starting actor with engine: $engine")
	}
}
