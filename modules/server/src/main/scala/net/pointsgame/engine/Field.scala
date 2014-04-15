// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.engine

import scala.collection.immutable
import scala.collection.mutable.ListBuffer


protected class Field(val sizeX: Int, val sizeY: Int) {

	/**
	 * "border" dot. This dot is neighbour for all dots
	 * that otherwise would fall out of array range
	 */
	private val e = new Dot(-1, -1, -1)

	private val field = immutable.Vector.
			tabulate[Dot](sizeX * sizeY) { i =>
		new Dot(i, i % sizeX, i / sizeX)
	}

	def dotsIterator = field.iterator

	def apply(x: Int, y: Int): Dot = field(x + y * sizeX)

	for (x <- 0 until sizeX; y <- 0 until sizeY) {
		val dot = this(x, y)
		dot.u = e
		dot.ur = e
		dot.r = e
		dot.rd = e
		dot.d = e
		dot.dl = e
		dot.l = e
		dot.lu = e
	}

	for (x <- 0 until sizeX; y <- 1 until sizeY) this(x, y).u = this(x, y - 1)
	for (x <- 0 until sizeX - 1; y <- 1 until sizeY) this(x, y).ur = this(x + 1, y - 1)
	for (x <- 0 until sizeX - 1; y <- 0 until sizeY) this(x, y).r = this(x + 1, y)
	for (x <- 0 until sizeX - 1; y <- 0 until sizeY - 1) this(x, y).rd = this(x + 1, y + 1)
	for (x <- 0 until sizeX; y <- 0 until sizeY - 1) this(x, y).d = this(x, y + 1)
	for (x <- 1 until sizeX; y <- 0 until sizeY - 1) this(x, y).dl = this(x - 1, y + 1)
	for (x <- 1 until sizeX; y <- 0 until sizeY) this(x, y).l = this(x - 1, y)
	for (x <- 1 until sizeX; y <- 1 until sizeY) this(x, y).lu = this(x - 1, y - 1)

	def toPicture = {
		val result = Vector.fill(sizeY * 3)(ListBuffer.fill(sizeX * 3)(' '))
		for (x <- 0 until sizeX; y <- 0 until sizeY) {
			this(x, y).dotType match {
				case Dot.empty => result(x * 3 + 1)(y * 3 + 1) = ' '
				case Dot.blueActive => result(x * 3 + 1)(y * 3 + 1) = 'o'
				case Dot.redActive => result(x * 3 + 1)(y * 3 + 1) = 'x'
				case Dot.blueTired => result(x * 3 + 1)(y * 3 + 1) = 'O'
				case Dot.redTired => result(x * 3 + 1)(y * 3 + 1) = 'X'
				case Dot.blueDefeated => result(x * 3 + 1)(y * 3 + 1) = '#'
				case Dot.redDefeated => result(x * 3 + 1)(y * 3 + 1) = '@'
				case Dot.emptyEatenByBlue => result(x * 3 + 1)(y * 3 + 1) = ','
				case Dot.emptyEatenByRed => result(x * 3 + 1)(y * 3 + 1) = '\''
				case Dot.blueCtrl => result(x * 3 + 1)(y * 3 + 1) = '?'
				case Dot.redCtrl => result(x * 3 + 1)(y * 3 + 1) = '¿'
			}
			if (this(x, y).treeNext != null) {
				(this(x, y).treeNext.x - this(x, y).x, this(x, y).treeNext.y - this(x, y).y) match {
					case (1, 0) => result(x * 3 + 2)(y * 3 + 1) = '-'
					case (1, 1) => result(x * 3 + 2)(y * 3 + 2) = '\\'
					case (0, 1) => result(x * 3 + 1)(y * 3 + 2) = '|'
					case (-1, 1) => result(x * 3 + 1)(y * 3 + 2) = '/'
					case (-1, 0) => result(x * 3 + 1)(y * 3 + 2) = '-'
					case (-1, -1) => result(x * 3 + 1)(y * 3 + 2) = '\\'
					case (0, -1) => result(x * 3 + 1)(y * 3 + 2) = '|'
					case (1, -1) => result(x * 3 + 1)(y * 3 + 2) = '/'
				}
			}
		}
		result.map(String.valueOf)
	}

}
