// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.comet

import akka.actor.actorRef2Scala
import net.liftweb.common.Loggable
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.jquery.JqJE.{JqAppend, JqScrollToBottom, JqId}
import net.pointsgame.actor._
import net.pointsgame.db.Message
import net.pointsgame.helpers.HtmlHelpers
import scala.xml.NodeSeq

class ChatComet extends CometActor with Loggable {
	private var msgs = Vector[Message]()

	lazy val roomActor = Root.getRoom(this.name openOr "")

	override protected def localSetup() {
		roomActor ! AddChatSubscription(this)
		super.localSetup()
	}

	override protected def localShutdown() {
		roomActor ! RemoveChatSubscription(this)
		super.localShutdown()
	}

	override def lowPriority = {
		case v: Vector[_] =>
			val oldM = msgs
			msgs = v.asInstanceOf[Vector[Message]]
			val added = oldM.lastOption.map { last =>
				msgs.reverse.takeWhile(last != _).reverse
			}.getOrElse(msgs)
			partialUpdate(myUpdate(added))
	}

	private def append(x: NodeSeq) = JqId("chat_messages") ~> JqAppend(x)

	private def myUpdate(l: Seq[Message]) = {
		val appendJs = l.map(transform).
				map(append).map(_.cmd).fold(Noop)(_ & _)
		appendJs & JqId("chat_messages") ~> JqScrollToBottom
	}

	private val transform = HtmlHelpers.seqMemoize { e: Message =>
		".time *" #> e.shortTime &
				".nick *" #> e.user &
				".content *" #> e.content
	}


	def render = ".chatLine" #> transform.list(msgs)

}

