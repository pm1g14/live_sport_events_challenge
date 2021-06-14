package domain

import java.time.{Duration}

case class Match(state: MatchState)

case class MatchState(events: Seq[Event])

case class Event(lastScored:String, pointsScored:Int, matchTime:Duration,  team1P: Int, team2P: Int, matchScore:String)
