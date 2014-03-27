// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.actor

import akka.actor.Actor
import net.liftweb.common.Loggable
import net.pointsgame.db.Pointsgame._
import net.pointsgame.db.{MessageFunctions, DBUser, Message, DBLibrary}
import net.pointsgame.lift.comet.ChatComet
import scala.collection.immutable
import scala.collection.mutable

class Room(val name: String) extends Actor with Loggable {

	logger.info("room actor initialized")
	protected var users = immutable.HashSet[DBUser]()

	override def preStart() {}

	def receive: Receive = {
		case "" =>
	}
}

trait Chatting extends Room {
	logger.info("room chatting actor initialized")
	val listeners = mutable.LinkedHashSet[ChatComet]()
	var msgs = immutable.Vector[Message]()

	msgs = MessageFunctions.loadRoom(name)

	override def receive: Receive = super.receive.orElse {
		case m: Message =>
			logger.info(s"room $name: received chat, writing to DB: $m")
			transaction {
				DBLibrary.messages.insert(m)
			}
			msgs = msgs :+ m takeRight 50
			listeners.foreach(_ ! msgs)
		case AddChatSubscription(chatComet) =>
			logger.debug(s"room $name we've been subscribed:) $chatComet")
			listeners += chatComet
			chatComet ! msgs
		case RemoveChatSubscription(chatComet) =>
			logger.debug(s"room $name we've been unsubscribed:( $chatComet")
			listeners -= chatComet
	}
}

trait Gaming extends Room {
}
