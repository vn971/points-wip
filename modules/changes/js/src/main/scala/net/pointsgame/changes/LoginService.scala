package net.pointsgame.changes

import scala.concurrent.Future
import scala.{Either ⇒ \/}

trait LoginService {
	def register(username: String, email: String): Future[Error \/ User]
}
