// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.actor

import akka.actor.Actor
import net.liftweb.common.Loggable

object ServerState extends Actor with Loggable {
	logger.info("'ServerState' initialized")
	private val usersOnline = List() // user, room

	def receive: Receive = {
		case _ => Unit
	}
}
