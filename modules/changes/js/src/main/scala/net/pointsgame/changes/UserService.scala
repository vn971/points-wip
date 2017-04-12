package net.pointsgame.changes

import monix.reactive.Observable
import net.pointsgame.changes.Model._

trait UserService {
	def myPrivateMessages(me: String): Observable[NewPrivateMessage]
}
