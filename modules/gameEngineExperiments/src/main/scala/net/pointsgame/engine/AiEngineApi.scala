package net.pointsgame.engine


abstract sealed class MoveResult
case object Error extends MoveResult
case object Normal extends MoveResult
case class WithSurroundings(newSurroundings: Int*) extends MoveResult
