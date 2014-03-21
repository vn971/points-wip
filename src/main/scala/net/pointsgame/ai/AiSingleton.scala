package net.pointsgame.ai

import akka.actor._
import net.pointsgame.lift.comet._

object MyActorSys {
}

object liftGameData {
	private val system = ActorSystem()
	val ai = system.actorOf(akka.actor.Props[DuelWithEngine], name = "ai")

}

