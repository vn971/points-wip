package net.pointsgame.lift.model

case class SgfGame(sizeX: Int, sizeY: Int, moves: Seq[(Boolean, Int, Int)])
