package net.pointsgame.lift.comet

import net.liftweb.actor._
import net.liftweb.common.Logger
import net.liftweb.http._
import net.pointsgame.lift.model._

object PaperSingleton extends LiftActor with ListenerManager with Logger {

	private var l: List[PaperEvent] = List()

	def createUpdate = l

	override def lowPriority = {
		case m: PaperEvent =>
			debug("received paper event: "+m)
			l = l :+ m
			updateListeners()
	}
}
