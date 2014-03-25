package net.pointsgame.ratings

trait RatingUserInfo {
		def id: Long
		def name: String
		def ratingPrecision: Double
}

case class RatingGameInfo(
		first: RatingUserInfo, second: RatingUserInfo,
		stupidResult: StupidResult)

abstract sealed class StupidResult
case object FirstWon extends StupidResult
case object SecondWon extends StupidResult
case object Draw extends StupidResult
