package adapters.impl

import java.time.Duration

import domain.{Event, Match, MatchState}
import utils.DataConversionUtils
import validate.ResponseValidator

import scala.util.Try

class RawToMatchEventsAdapter(rawMatchEvents: Stream[String], validator: ResponseValidator) {

  private val pointsScoredPos: (Int, Int) = (30, 31)
  private val whoScoredPos: Int = 29
  private val team2PointsPos: (Int, Int) = (21, 28)
  private val team1PointsPos: (Int, Int) = (13, 20)
  private val elapsedTimePos: (Int, Int) = (1, 12)


  def convertRawToMatchEvents: Match = {
    val binaryRepresentation = rawMatchEvents.map(DataConversionUtils.convertHexStringToBin)
    val matchEvents: Stream[Event] = binaryRepresentation.map(x => {
        val pointsScoredSoFar: Int = DataConversionUtils.binToInt(splitOnIndices(pointsScoredPos, x))
        val team1Points: Int = DataConversionUtils.binToInt(splitOnIndices(team1PointsPos, x))
        val team2Points: Int = DataConversionUtils.binToInt(splitOnIndices(team2PointsPos, x))
        val elapsedTime: Long = DataConversionUtils.binToInt(splitOnIndices(elapsedTimePos, x)).toLong
        val lastTeamScored: String = if (x.charAt(whoScoredPos).toString.equals("0")) "team1" else "team2"
        Event(
          lastScored = lastTeamScored,
          pointsScored = pointsScoredSoFar,
          matchTime = Duration.ofSeconds(elapsedTime.toLong),
          team1P = team1Points,
          team2P = team2Points,
          matchScore = s"$team1Points - $team2Points"
        )
      }
    )
    val matchState = MatchState(events = filterOutInvalidEvents(matchEvents.sortBy(_.matchTime)))
    Match(state = matchState)
  }

  private[adapters] def filterOutInvalidEvents(events: Seq[Event]): Seq[Event] = {
    events.foldLeft(Seq.empty[Event])((result, event) => {
      val lastEvent = Try(result.last).toOption
      if (validator.validate(
        event = event,
        lastEvent = lastEvent)) {
          result :+ event
      }
      else result
    })
  }

  private[adapters] def splitOnIndices(limits: (Int, Int), stringToSplit: String): String = {
    val lower = limits._1
    val upper = limits._2
    Try(stringToSplit.substring(lower, upper + 1)).toOption match {
      case Some(value) => value
      case _           => ""
    }
  }

}
