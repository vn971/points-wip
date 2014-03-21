package net.pointsgame.ratings

import net.liftweb.common.Loggable
import net.pointsgame.db.DBLibrary._
import net.pointsgame.ratings.RatingConstants._
import org.squeryl.PrimitiveTypeMode._


/**
 * the code is a little terrible here:
 * I need it to work really quick, for tests.
 */
object Ratings extends Loggable {

	def ternarySearch(f: Long => Double) = {
		var (left, right) = (minimumRating, maximumRating)
		while (right - left >= 3) {
			val leftThird = (left * 2 + right) / 3
			val rightThird = (left + right * 2) / 3
			if (f(leftThird) < f(rightThird)) {
				left = leftThird
			} else {
				right = rightThird
			}
		}
		left
	}

	@inline def probabilityToWin(myRating: Long, oppRating: Long, precision: Double) =
		1 - precision * oppRating / (oppRating + myRating)

	@inline def probabilityToLose(myRating: Long, oppRating: Long, precision: Double) =
		1 - precision * myRating / (oppRating + myRating)

	def refineRating(userId: Long, now: Long = System.currentTimeMillis) {

		@inline def packPrecisionDate(prec: Double, date: Long) = {
			//						exp((now - date).toDouble / decreaseImpactTime) * prec
			1.0
		}

		def gamesWithFirstPlayer(user: Long, firstWon: Boolean) =
			transaction {
				from(games, users)((game, opponent) =>
					where(
						game.first === user and
								game.second === opponent.id and
								game.wonFirst === Some(firstWon)
					)
							select(opponent.rating, packPrecisionDate(opponent.ratingPrecision, game.date.getTime))
				).toList
			}

		def gamesWithSecondPlayer(user: Long, firstWon: Boolean) =
			transaction {
				from(games, users)((game, opponent) =>
					where(
						game.first === opponent.id and
								game.second === user and
								game.wonFirst === Some(firstWon)
					)
							select(opponent.rating, packPrecisionDate(opponent.ratingPrecision, game.date.getTime))
				).toList
			}

		val winList = gamesWithFirstPlayer(userId, firstWon = true) :::
				gamesWithSecondPlayer(userId, firstWon = false)

		val lossList = gamesWithFirstPlayer(userId, firstWon = false) :::
				gamesWithSecondPlayer(userId, firstWon = true)

		def probabilityOfRating(myRating: Long) = {
			var result = 1.0d

			for ((opponentRating, precision) <- winList) {
				result *= probabilityToWin(myRating, opponentRating, precision)
			}
			result *= probabilityToWin(myRating, defaultRating, attractionToDefault)

			for ((opponentRating, precision) <- lossList) {
				result *= probabilityToLose(myRating, opponentRating, precision)
			}
			result *= probabilityToLose(myRating, defaultRating, attractionToDefault)

			result
		}

		val newRating = ternarySearch(probabilityOfRating)

		transaction {
			users.update(u =>
				where(u.id === me)
					set(u.rating := newRating, u.ratingPrecision := 1.0)
			)
		}

	}

	def refineAllRatings() {
		val userList = transaction {
			users.toList
		}
		userList.par.foreach(u => Ratings.refineRating(u.id))
	}

}
