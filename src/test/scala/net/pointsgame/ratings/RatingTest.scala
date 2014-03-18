package net.pointsgame.ratings

import net.liftweb.common.Loggable
import net.pointsgame.db.DBFunctions._
import net.pointsgame.db.DBLibrary._
import net.pointsgame.db._
import net.pointsgame.ratings.RatingConstants._
import org.scalatest._
import org.squeryl.PrimitiveTypeMode._

class RatingTest extends FunSuite with Loggable with BeforeAndAfterAll {

	test("probability simple check") {
		// 0.666
		assert(Ratings.probabilityToWin(1000L, 500L, 1.0) > 0.666)
		assert(Ratings.probabilityToWin(1000L, 500L, 1.0) < 0.7)

		// 0.333
		assert(Ratings.probabilityToLose(1000L, 500L, 1.0) > 0.333)
		assert(Ratings.probabilityToLose(1000L, 500L, 1.0) < 0.4)
	}

	override def beforeAll() {
		DBSetUp.setUp("jdbc:h2:mem:tests")
		DBSetUp.createDB()
	}

	override def afterAll() {
		DBSetUp.dropDB()
	}

	test("insert game") {
		transaction {
			users.insert(DBUser("u01", "first player"))
			users.insert(DBUser("u02", "second player"))
		}
		transaction {
			games.insert(Game("u01", "u02", Some(true)))
		}
	}
	test("insert game with non-existing first player") {
		intercept[RuntimeException] {
			transaction {
				games.insert(Game("u01", "u02_nonexistent"))
			}
		}
	}

	test("insert game with non-existing second player") {
		intercept[RuntimeException] {
			transaction {
				games.insert(Game("u01_nonexistent", "u02"))
			}
		}
	}

	test("find games") {
		assert(DBFunctions.findGames("u01").nonEmpty)
		assert(DBFunctions.findGames("u02").nonEmpty)
		assert(DBFunctions.findGames("user_nonexistent").isEmpty)
	}

	test("every game increases the rating of the winner") {
		transaction {
			users.insert(DBUser("u03", ""))
			users.insert(DBUser("u04", "", humanToLong(3000)))
		}

		var ratingBefore = transaction(users.get("u03").rating)

		for (i <- 1 to 3) {
			transaction {
				games.insert(Game("u03", "u04", Some(true)))
			}
			Ratings.refineRating("u03")

			val ratingNew = transaction(users.get("u03").rating)
			assert(ratingNew > ratingBefore)
			ratingBefore = ratingNew
		}
	}

	ignore("survives lots of games") {
		val gameList = (1 to 100).map(i => Game("u03", "u04"))
		transaction {
			games.insert(gameList)
		}
		Ratings.refineRating("u03")
	}

	ignore("ratings don't grow too fast") {
		logger debug "u04: " + getRating("u04")
		Ratings.refineRating("u03")
		assert(getRating("u03") > defaultRating * 3)
		assert(getRating("u03") < defaultRating * 120)
	}

	ignore("ratings don't differ much after 1 game") {
		transaction {
			users.insert(DBUser("u05"))
			users.insert(DBUser("u06", rating = humanToLong(3000)))
		}
		transaction {
			games.insert(Game("u05", "u06"))
		}
		for (i <- 1 to 10) {
			Ratings.refineRating("u05")
			Ratings.refineRating("u06")
		}
		assert(getRating("u05") > defaultRating / 2)
		assert(getRating("u05") < defaultRating * 2)

		assert(getRating("u06") > defaultRating / 2)
		assert(getRating("u06") < defaultRating * 2)
	}

}
