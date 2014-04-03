// This project is licensed under GPL, version 3 or later. See license.txt for more details.
//
// Copyright: Vasya Novikov 2013-2014.

package net.pointsgame.ratings

import scala.concurrent.duration._
import _root_.scala.math._
import net.liftweb.common.Loggable

object RatingConstants extends Loggable {

	private lazy val defHumanRating = 700
	private lazy val minHumanRating = 300
	private lazy val expConst = 400
	private lazy val defComputerRating = 100L

	// we choose the correct constants now, by using math and magic
	// if you'll want to recreate this formula -- look and understand the "longToHuman" formula
	private lazy val minRatingConstant = {
		val ratingDiff = defHumanRating - minHumanRating
		val expp = exp(ratingDiff.toDouble / expConst)
		(defComputerRating / (expp - 1)).round
	}
	private lazy val humanRatingShift =
		(defHumanRating - log(defComputerRating + minRatingConstant) * expConst).round.toInt


	def longToHuman(r: Long) = (log(r + minRatingConstant) * expConst).round.toInt + humanRatingShift

	def humanToLong(r: Int) = exp((r - humanRatingShift).toDouble / expConst).round - minRatingConstant

	lazy val defaultRating = humanToLong(defHumanRating)
	lazy val forgetGameTime = 365.days.toMillis
	lazy val decreaseImpactTime = 40.days.toMillis
	lazy val attractionToDefault = 1.0

	lazy val minimumRating = 1L
	lazy val maximumRating = defComputerRating * 100000 // in practice, it's around 30 000
}
