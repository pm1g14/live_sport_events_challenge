package validate.impl

import java.time.Duration

import domain.Event
import org.scalatest.FunSuite

class MatchEventRawResponseValidatorTest extends FunSuite {

  test("validateIndividual is invoked with valid Event, should return true") {
    val event = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    )
    assert(new MatchEventRawResponseValidator().validateIndividual(event))
  }

  test("validateIndividual is invoked with event that has wrong values, should return false") {
    val event = Event(
      lastScored = "team1",
      pointsScored = 20,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    )
    assert(!new MatchEventRawResponseValidator().validateIndividual(event))

    val event2 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(5000L),
      matchScore = "2 - 0"
    )
    assert(!new MatchEventRawResponseValidator().validateIndividual(event2))
  }

  test("compareWithPrevious is invoked for 2 events should return true for valid events") {
    val event1 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    )
    val event2 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 4,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "4 - 0"
    )
    assert(new MatchEventRawResponseValidator().compareWithPrevious(Some(event1), event2))
  }

  test("compareWithPrevious is invoked with 1 event, should return true for valid event") {
    val event1 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    )
    assert(new MatchEventRawResponseValidator().compareWithPrevious(None, event1))
  }

  test("compareWithPrevious is invoked with invalid score comparison between 2 events, should return false") {
    val event1 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    )
    val event2 = Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 6,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "6 - 0"
    )
    assert(!new MatchEventRawResponseValidator().compareWithPrevious(Some(event1), event2))
  }

}
