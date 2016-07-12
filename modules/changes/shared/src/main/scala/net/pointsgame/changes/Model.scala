package net.pointsgame.changes


case class User(id: Long)
case class Channel(id: Long)
case class UserConnected(channel: Channel, user: User)
