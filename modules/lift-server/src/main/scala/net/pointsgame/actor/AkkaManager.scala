package net.pointsgame.actor

import akka.actor.{ActorRef, ActorSystem, Actor, Props}
import net.liftweb.common.Loggable
import scala.collection.mutable

object AkkaManager extends Loggable {
	private val system = ActorSystem("root")
	private val roomList = mutable.HashMap[String, ActorRef]()
	//	private val roomSupervisor = system.actorOf(Props[Rooms], "rooms")

	def getRoom(name: String) =
		synchronized {
			roomList.getOrElseUpdate(name,
				system.actorOf(
					Props(new Room(name) with Chatting with Gaming), name))
			//roomSupervisor.path./(name).toString))
		}

	def shutdown(): Unit = {
		system.shutdown()
	}
}

class Rooms extends Actor with Loggable {
	def receive = {
		case m@_ => logger.error(s"room actor received unknown message $m")
	}
}
