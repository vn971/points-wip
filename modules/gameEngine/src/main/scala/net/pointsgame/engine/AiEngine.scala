// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.engine

import scala.collection.mutable

// This class needs to be very quick.
// Nulls and other ugly stuff is used here.

/**
 * this class is not thread-safe
 */
class AiEngine(val sizeX: Int, val sizeY: Int) {
	var surroundingId = 0

	val field = new Field(sizeX, sizeY)

	def makeMove(xNewCoordinate: Int, yNewCoordinate: Int, red_? : Boolean): MoveResult = {
		val dot = field(xNewCoordinate, yNewCoordinate)
		if (!dot.isFreeToPlace) return Error

		val newColor = if (red_?) Dot.redActive else Dot.blueActive
		dot.dotType = newColor

		val branches = mutable.ListBuffer[Dot]()

		{
			val u = dot.u.isPowered(red_?)
			val ur = dot.ur.isPowered(red_?)
			val r = dot.r.isPowered(red_?)
			val rd = dot.rd.isPowered(red_?)
			val d = dot.d.isPowered(red_?)
			val dl = dot.dl.isPowered(red_?)
			val l = dot.l.isPowered(red_?)
			val lu = dot.lu.isPowered(red_?)
			if (ur && !r) {
				dot.treeNext = dot.ur
				branches += dot.ur
			} else if (r && !rd && !d) {
				dot.treeNext = dot.r
				branches += dot.r
			}
			if (rd && !d) {
				dot.treeNext = dot.rd
				branches += dot.rd
			} else if (d && !dl && !l) {
				dot.treeNext = dot.d
				branches += dot.d
			}
			if (dl && !l) {
				dot.treeNext = dot.dl
				branches += dot.dl
			} else if (l && !lu && !u) {
				dot.treeNext = dot.l
				branches += dot.l
			}
			if (lu && !u) {
				dot.treeNext = dot.lu
				branches += dot.lu
			} else if (u && !ur && !r) {
				dot.treeNext = dot.u
				branches += dot.u
			}
			//if (dot.ur.isPowered(red_?) && !dot.r.isPowered(red_?)) branches += dot.ur
			//if (dot.r.isPowered(red_?) && !dot.rd.isPowered(red_?)) branches += dot.r
			//if (dot.rd.isPowered(red_?) && !dot.d.isPowered(red_?)) branches += dot.rd
			//if (dot.d.isPowered(red_?) && !dot.dl.isPowered(red_?)) branches += dot.d
			//if (dot.dl.isPowered(red_?) && !dot.l.isPowered(red_?)) branches += dot.dl
			//if (dot.l.isPowered(red_?) && !dot.lu.isPowered(red_?)) branches += dot.l
			//if (dot.lu.isPowered(red_?) && !dot.u.isPowered(red_?)) branches += dot.lu
		}

		if (branches.size <= 1) return Normal

		dot.treeNext = null

		val grouped = branches.groupBy(_.getRoot.id)
		var surroundings_? = false
		for (group <- grouped.values) {
			if (group.size == 2) {
				surroundings_? = true
			}
			for (firstBranchIndex <- 0 until group.size;
					 secondBranchIndex <- firstBranchIndex until group.size) {
				val first = group(firstBranchIndex)
				val second = group(secondBranchIndex)
				surroundingId += 1

				{
					var d = first
					while (d != null) {
						d.surroundingId = surroundingId
						d = d.treeNext
					}
				}

				val realRoot = {
					var realRoot = second
					while (realRoot.surroundingId != surroundingId) {
						realRoot = realRoot.treeNext
					}
					realRoot
				}

				var minX, maxX = dot.x
				var minY, maxY = dot.y
				surroundingId += 1

				@inline
				def addBorder(from: Dot, to: Dot): Unit = {
					minX = minX min to.x
					maxX = maxX max to.x
					minY = minY min to.x
					maxY = maxY max to.x
					(to.x - from.x, to.y - from.y) match {
						case (0, -1) =>
						case (1, -1) => field(from.x, from.y).surroundingId = surroundingId
						case (1, 0) => field(from.x, from.y).surroundingId = surroundingId
						case (1, 1) => field(from.x, from.y + 1).surroundingId = surroundingId
						case (0, 1) =>
						case (-1, 1) => field(from.x - 1, from.y + 1).surroundingId = surroundingId
						case (-1, 0) => field(from.x - 1, from.y).surroundingId = surroundingId
						case (-1, -1) => field(from.x - 1, from.y).surroundingId = surroundingId
					}
					if (field(from.x, to.y).isPowered(!red_?) && field(from.y, to.x).isPowered(!red_?)) {
						if (field(from.x, from.y).treeNext == field(to.x, to.y)) field(from.x, from.y).treeNext = null
						if (field(to.x, to.y).treeNext == field(from.x, from.y)) field(to.x, to.y).treeNext = null
					}
				}

				def addBorders(target: Dot): Unit = {
					var d = target
					var next = d.treeNext
					while (d != realRoot) {
						addBorder(d, next)
						d = next
						next = next.treeNext
					}
				}

				addBorders(first)
				addBorders(second)
				addBorder(dot, first)
				addBorder(dot, second)

				{
					var inside_? = false
					for (x <- minX to maxX) {
						inside_? = false
						for (y <- minY to maxY) {
							if (inside_?) Dot.eat(dot, red_?)
						}
					}
				}
			}
			Dot.makeRoot(dot, group(0))
		}

		if (surroundings_?) {
			WithSurroundings()
		} else {
			Normal
		}
	}

	def redScore: Int = {
		field.dotsIterator.count(_.dotType == Dot.blueDefeated)
	}

	def blueScore: Int = {
		field.dotsIterator.count(_.dotType == Dot.redDefeated)
	}

	//def apply(x: Int, y: Int) = field(x, y).dotType

	def toPicture: Vector[String] = field.toPicture

}


/**
 * TODO
 *
 * Dot operations optimization
 *
 * branches calculation, like in this position
 * .x.
 * .Xx
 * ...
 *
 * after tests and benchmarking would be done --
 * try to replace dotType with Scala-2.10 value class
 *
 */
