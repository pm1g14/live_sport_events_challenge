package validate.impl

import java.time.Duration

import domain.Event
import enum.PointsEnum
import enum.PointsEnum.PointsEnum
import validate.ResponseValidator

class MatchEventRawResponseValidator extends ResponseValidator {

  private val validScorePoints = Seq(PointsEnum.threePoints, PointsEnum.twoPoints, PointsEnum.onePoint)
  private val maxGameDurationInSecs = 2880L

  override def validate(event: Event, lastEvent: Option[Event] = None): Boolean = {
    validateIndividual(event) && compareWithPrevious(lastEvent = lastEvent, thisEvent = event)
  }

  private[validate] def validateIndividual(event: Event): Boolean = {
    if (Duration.ofSeconds(maxGameDurationInSecs).compareTo(event.matchTime) < 0) false
    else if (!validScorePoints.map(_.id).contains(event.pointsScored)) false
    else true
  }

  private[validate] def compareWithPrevious(lastEvent: Option[Event], thisEvent: Event): Boolean = {
    lastEvent match {
      case Some(value) =>
        val lastTeam1Score = value.team1P
        val lastTeam2Score = value.team2P
        val thisTeam1Score = thisEvent.team1P
        val thisTeam2Score = thisEvent.team2P
        val team1ScoreDiff = thisTeam1Score - lastTeam1Score
        val team2ScoreDiff = thisTeam2Score - lastTeam2Score
        (team1ScoreDiff <= PointsEnum.threePoints.id && team2ScoreDiff <= PointsEnum.threePoints.id) && (team1ScoreDiff >= 0 && team2ScoreDiff >= 0)
      case _ => true
    }

  }
}
