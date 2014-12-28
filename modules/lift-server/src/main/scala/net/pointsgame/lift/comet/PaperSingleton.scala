package net.pointsgame.lift.comet

import net.liftweb.actor._
import net.liftweb.common.Logger
import net.liftweb.http._
import net.pointsgame.lift.model._

object PaperSingleton extends LiftActor with ListenerManager with Logger {

	case class ListPaperEvent(l: List[PaperEvent])

	private var l: List[PaperEvent] = List()

	protected def createUpdate = ListPaperEvent(l)

	override protected def lowPriority = {
		case m: PaperEvent =>
			debug(s"received paper event: $m")
			l = l :+ m
			updateListeners()
	}
}
