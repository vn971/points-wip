package net.pointsgame.changes


object Model {
	trait DateTime
	case class User(name: String) extends AnyVal

	case class NewPrivateMessage(creator: String, source: String, target: String, createdAt: DateTime)

}
