// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.lift.model

case class SgfGame(sizeX: Int, sizeY: Int, moves: Seq[(Boolean, Int, Int)])
