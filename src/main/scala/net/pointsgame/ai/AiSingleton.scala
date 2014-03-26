// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.ai

import akka.actor._

object AiSingleton {
	private val system = ActorSystem()
	val duel = system.actorOf(akka.actor.Props[DuelWithEngine], name = "duel")
}
