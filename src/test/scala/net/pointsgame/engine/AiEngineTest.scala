package net.pointsgame.engine

import java.util
import net.liftweb.common.Loggable
import org.scalatest.Inside._
import org.scalatest._
import scala.collection.JavaConversions._

class AiEngineTest extends FunSuite with Loggable {

	case class Dot(x: Int, y: Int, red_? : Boolean)

	def constructGame(image: String) = {
		val lines = image.stripMargin.linesIterator.toArray.filter(_.nonEmpty)

		val moves = new util.TreeMap[Char, Dot]()
		for (y <- lines.indices; x <- lines(y).indices) {
			val char = lines(y)(x)
			if (char.toUpper != char.toLower) {
				moves.put(char.toLower, Dot(x, y, char.isUpper))
			}
		}
		moves.values
	}

	def constructResults(game: util.Collection[Dot]) = {
		val engine = new AiEngine(100, 100)
		game.map(dot => engine.makeMove(dot.x, dot.y, dot.red_?)).toList
	}

	def play(s: String) = constructResults(constructGame(s.stripMargin))

	/**
	 * test name is equal the the graphical game representation
	 */
	def testGame(s: String, m: PartialFunction[List[MoveResult], Unit]) {
		test(s.drop(2)) {
			inside(play(s))(m)
		}
	}

	testGame(
	"""
		.a.
		..b
		dc.
	""", {
		case Normal :: Normal :: Normal :: Normal :: Nil =>
	})

	testGame(
	"""
		ibc
		had
		gfe
	""", {
		case l if l == List.fill(9)(Normal) =>
	})

	test("surroundEmpty") {
		val engine = new AiEngine(5, 5)
		engine.makeMove(1, 0, red_? = true)
		engine.makeMove(2, 1, red_? = true)
		engine.makeMove(1, 2, red_? = true)
		engine.makeMove(0, 1, red_? = true)
		assert(engine.redScore == 0)
		assert(engine.blueScore == 0)
	}

	testGame(
	"""
		.a.
		d.b
		.c.
	""", {
		case Normal :: Normal :: Normal :: WithSurroundings() :: Nil =>
	})

	testGame(
	"""
		azb
	""", {
		case Normal :: Normal :: Normal :: Nil =>
	})

	testGame(
	"""
		...c..
		abx.d.
		...e..
	""", {
		case Normal :: Normal :: Normal :: Normal :: Normal :: WithSurroundings() :: Nil =>
	})

	testGame(
	"""
		.cde..
		b...f.
		a.r..g
		.x.q.h
		o.p..i
		n...j.
		.mlk..
	""", {
		case Normal :: Normal :: Normal :: Normal :: Normal ::
			Normal :: Normal :: Normal :: Normal :: Normal ::
			Normal :: Normal :: Normal :: Normal :: Normal ::
			Normal :: Normal :: Normal :: WithSurroundings() :: Nil =>
	})

	//test("chain") {
	//	play( """
	//					|.a.
	//					|..b
	//					|dc.
	//				""") should be === List.fill(4)(Normal)
	//}
	//

}
