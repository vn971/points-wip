package net.pointsgame

import java.util
import net.liftweb.common.Loggable
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult._
import scala.collection.JavaConversions._


class OldSingleGameEngineTest extends FunSuite with Loggable with ShouldMatchers {

	case class Dot(x: Int, y: Int, red_? : Boolean)

	def constructGame(image: String) = {
		val lines = image.stripMargin.linesIterator.toArray.filter(_.nonEmpty)

		val moves = new util.TreeMap[Char, Dot]()
		for (y <- lines.indices; x <- lines(y).indices) {
			val char = lines(y)(x)
			if (char.toUpper != char.toLower) {
				moves.put(char.toLower, Dot(x + 1, y + 1, char.isUpper))
			}
		}
		moves.values
	}

	def constructResults(game: util.Collection[Dot]) = {
		val engine = new SingleGameEngine(100, 100)
		game.map(dot => engine.makeMove(dot.x, dot.y, dot.red_?)).toList
	}

	def play(s: String) = constructResults(constructGame(s.stripMargin))

	def shouldMatch(p: PartialFunction[List[MoveResult], Unit]):
	List[MoveResult] => Boolean =
		l => p.isDefinedAt(l) && p.lift.apply(l).nonEmpty

	/**
	 * test name is equal the the graphical game representation
	 */
	def testGame(s: String, tests: (List[MoveResult] => Boolean)*) {
		test(s.drop(2)) {
			val playedGame = play(s)
			tests.foreach(func => assert(func(playedGame)))
		}
	}

	testGame(
		"""
		.a.
		..b
		dc.
		""", shouldMatch {
			case NOTHING :: NOTHING :: NOTHING :: NOTHING :: Nil =>
		}, _.size == 4)

	testGame(
		"""
		ibc
		had
		gfe
		""",
		_ == List.fill(9)(NOTHING)
	)

	testGame(
		"""
		.a.
		d.b
		.c.
		""", shouldMatch {
			case NOTHING :: NOTHING :: NOTHING :: GOOD :: Nil =>
		})

	testGame(
		"""
		azb
		""", shouldMatch {
			case NOTHING :: NOTHING :: NOTHING :: Nil =>
		})

	testGame(
		"""
		...c..
		abx.d.
		...e..
		""", shouldMatch {
			case NOTHING :: NOTHING :: NOTHING :: NOTHING :: NOTHING :: GOOD :: Nil =>
		})

	test("control surroundings") {
		val engine = new SingleGameEngine(100, 100)
		engine.makeMove(1, 0, true)
		engine.makeMove(2, 1, true)
		engine.makeMove(1, 2, true)
		engine.getSurroundings.size() should be === 0
		engine.makeMove(0, 1, true)
		engine.getSurroundings.size() should be === 0

		engine.makeMove(11, 11, true)
		engine.makeMove(11, 10, false)
		engine.makeMove(12, 11, false)
		engine.makeMove(11, 12, false)
		engine.getSurroundings.size() should be === 0
		engine.makeMove(10, 11, false)
		engine.getSurroundings.size() should be === 1
		engine.makeMove(1, 1, false)
		engine.getSurroundings.size() should be === 1
	}

	test("ctrl2") {
		val engine = new SingleGameEngine(100, 100)
		engine.makeMove(2, 2, false)
		engine.makeMove(12, 2, true)
		engine.makeMove(3, 1, false)
		engine.makeMove(11, 1, true)
		engine.makeMove(4, 2, false)
		engine.makeMove(10, 2, true)
		engine.makeMove(11, 2, false)
		engine.makeMove(1, 10, true)
		engine.makeMove(3, 3, false)

		engine.getSurroundings.size() should be === 0
		engine.makeMove(11, 3, true)
		engine.getSurroundings.size() should be === 1

		engine.makeMove(10, 3, false)

		engine.getSurroundings.size() should be === 1
		engine.makeMove(3, 2, true)
		engine.getSurroundings.size() should be === 2
		assert(engine.getSurroundings.get(0) != engine.getSurroundings.get(1))
		Console println "surr0: "+engine.getSurroundings.get(0).path
		Console println "surr1: "+engine.getSurroundings.get(1).path
	}

	testGame(
		"""
		.cde..
		b...f.
		a.r..g
		.x.q.h
		o.p..i
		n...j.
		.mlk..
		""", shouldMatch {
			case NOTHING :: NOTHING :: NOTHING :: NOTHING :: NOTHING ::
				NOTHING :: NOTHING :: NOTHING :: NOTHING :: NOTHING ::
				NOTHING :: NOTHING :: NOTHING :: NOTHING :: NOTHING ::
				NOTHING :: NOTHING :: NOTHING :: GOOD :: Nil =>
		})

}
