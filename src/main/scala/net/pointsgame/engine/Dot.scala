package net.pointsgame.engine

import net.pointsgame.engine.Dot._
import scala.annotation.tailrec


// This class needs to be very quick.
// Nulls and other strange stuff is used here.

//public class DotType

protected class Dot(

	// dots in directions: up, right, down, left
	//var u: Dot, var ur: Dot, var r: Dot, var rd: Dot,
	//var d: Dot, var dl: Dot, var l: Dot, var lu: Dot,

	//var treeNext: Dot, var treeJump: Dot,
	val id: Int,
	val x:Int, val y:Int
	) {
	var dotType: Int = empty

	var u, ur, r, rd, d, dl, l, lu: Dot = null
	var treeNext: Dot = null
	var surroundingId:Int = 0

	def isFreeToPlace = dotType == empty

	def isPowered(red: Boolean) =
		if (red) List(redActive, blueDefeated, emptyEatenByRed).contains(dotType)
		else List(blueActive, redDefeated, emptyEatenByBlue).contains(dotType)

	@tailrec
	final def getRoot: Dot = if (treeNext == null) this else treeNext.getRoot

	//@tailrec
	//final def makeRootFor(forWho: Dot) {
	//}

	override def toString = s"($x-$y)"
	override def hashCode() = id
	override def equals(a : Any) = this.id == a.asInstanceOf[Dot].id
}

protected object Dot {
	val empty = 0
	val blueActive = 1
	val redActive = 2
	val blueDefeated = 5
	val redDefeated = 6
	val emptyEatenByBlue = 7
	val emptyEatenByRed = 8

	// these shouldn't be used in an AI
	val blueTired = 3
	val redTired = 4
	val blueCtrl = 9
	val redCtrl = 10

	@tailrec
	def makeRoot(newRoot: Dot, forWho: Dot) {
		val nextNext = forWho.treeNext
		forWho.treeNext = newRoot
		if (nextNext != null) Dot.makeRoot(forWho, nextNext)
	}

	def eat(dot:Dot, red_? : Boolean) {
		val emp = if (red_?) emptyEatenByRed else emptyEatenByBlue
		val bl = if (red_?) blueDefeated else blueActive
		val rd = if (red_?) redDefeated else redActive
		Console println s"eating $dot with color ${if (red_?) "red" else "blue"}"
		dot.dotType = dot.dotType match {
			case `empty` => emp
			case `blueActive` => bl
			case `redActive` => rd
			case `blueDefeated` => bl
			case `redDefeated` => rd
			case `emptyEatenByBlue` => emp
			case `emptyEatenByRed` => emp
		}
	}

	//@inline def isAnyRed = (dotType & anyRed) != 0
	//@inline def isAnyBlue = (dotType & anyBlue) != 0
	//@inline def isFreeToPlace = (dotType & freeToPlace) != 0
	//private val anyRed = 1
	//private val anyBlue = 2
	//private val freeToPlace = 4
	//private val surroundedByRed = 8
	//private val surroundedByBlue = 16
	//private val bluePowered = 32
	//private val redPowered = 64
	//
	//val empty = freeToPlace
	//val blueActive = anyBlue | bluePowered
	//val redActive = anyRed | redPowered
	//val blueTired = anyBlue | surroundedByBlue | bluePowered
	//val redTired = anyRed | surroundedByRed | redPowered
	//val blueDefeated = anyBlue | surroundedByRed | redPowered
	//val redDefeated = anyRed | surroundedByBlue | bluePowered
	//val emptyEatenByBlue = surroundedByBlue | bluePowered
	//val emptyEatenByRed = surroundedByRed | redPowered
	//val blueCtrl = surroundedByBlue | freeToPlace | bluePowered
	//val redCtrl = surroundedByRed | freeToPlace | redPowered
}
