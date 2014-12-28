package net.pointsgame.ai

import akka.actor._

object AiSingleton {
	private val system = ActorSystem()
	val duel = system.actorOf(akka.actor.Props[DuelWithEngine], name = "duel")
}
