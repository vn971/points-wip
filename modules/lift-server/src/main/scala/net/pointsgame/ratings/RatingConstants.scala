package net.pointsgame.ratings

import scala.concurrent.duration._
import scala.math._

object RatingConstants {

	private lazy val defVisualRating = 700
	private lazy val minVisualRating = 300
	private lazy val expConst = 400
	private lazy val defComputerRating = 100L

	// we choose the correct constants now, by using math and magic
	// if you'll want to recreate this formula -- look and understand the "longToHuman" formula
	private lazy val minRatingConstant = {
		val ratingDiff = defVisualRating - minVisualRating
		val expp = exp(ratingDiff.toDouble / expConst)
		(defComputerRating / (expp - 1)).round
	}
	private lazy val humanRatingShift =
		(defVisualRating - log(defComputerRating + minRatingConstant) * expConst).round.toInt


	def longToHuman(r: Long) = (log(r + minRatingConstant) * expConst).round.toInt + humanRatingShift

	def humanToLong(r: Int) = exp((r - humanRatingShift).toDouble / expConst).round - minRatingConstant

	lazy val defaultRating = humanToLong(defVisualRating)
	lazy val forgetGameTime = 365.days.toMillis
	lazy val decreaseImpactTime = 40.days.toMillis
	lazy val attractionToDefault = 1.0

	lazy val minimumRating = 1L
	lazy val maximumRating = defComputerRating * 100000 // in practice, it's around 30 000
}
