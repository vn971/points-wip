// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.comet

import net.liftweb.common.Loggable
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.jquery.JqJE.{JqAppend, JqRemove, JqId}
import net.pointsgame.db.DBUser
import net.pointsgame.helpers.HtmlHelpers
import scala.collection.immutable

class UserListComet extends CometActor with Loggable {
	private var users = immutable.HashSet[DBUser](
		DBUser(name = "test name"),
		DBUser(name = "Frosya"),
		DBUser(name = "Vasya")
	)

	override protected def localSetup(): Unit = {
		super.localSetup()
	}

	override protected def localShutdown(): Unit = {
		super.localShutdown()
	}

	override def lowPriority = {
		case v: List[_] =>
			val oldUsers = users
			users = v.asInstanceOf[immutable.HashSet[DBUser]]

			val added = users -- oldUsers
			val removed = oldUsers -- users

			def addSingle(u: DBUser) = JqId("user_list") ~> JqAppend(transform(u))
			val addJs = added.map(addSingle).map(_.cmd).fold(Noop)(_ & _)

			def removeSingle(r: DBUser) = JqId("user_id_" + r.id) ~> JqRemove()
			val removeJs = removed.map(removeSingle).map(_.cmd).fold(Noop)(_ & _)

			partialUpdate(addJs & removeJs)
	}

	private val transform = HtmlHelpers.seqMemoize { e: DBUser =>
		".nick *" #> e.name &
				"div [id]" #> ("user_id_" + e.id) &
				".i [onclick]" #> ("alert('Информация о " + e.name + "');")
	}

	def render = ".userInList" #> transform.list(users.toSeq)

}
