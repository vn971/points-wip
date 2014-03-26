// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.engine


abstract sealed class MoveResult
case object Error extends MoveResult
case object Normal extends MoveResult
case class WithSurroundings(newSurroundings: Int*) extends MoveResult
