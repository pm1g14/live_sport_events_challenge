package adapters

import java.time.Duration

import adapters.impl.RawToMatchEventsAdapter
import domain.{Event, Match, MatchState}
import org.scalatest.FunSuite
import validate.impl.MatchEventRawResponseValidator

class RawToMatchEventsAdapterTest extends FunSuite {


  test("convertRawToMatchEvents is invoked with a list of raw events should return the correct match object") {
    val adapter = new RawToMatchEventsAdapter(Stream("0x801002", "0xf81016", "0x1d8102f"), new MatchEventRawResponseValidator)

    val actualMatch = adapter.convertRawToMatchEvents
    val expectedState = MatchState(events = Stream(
      Event(
        lastScored = "team1",
        pointsScored = 2,
        team1P = 2,
        team2P = 0,
        matchTime = Duration.ofSeconds(16L),
        matchScore = "2 - 0"
      ),
      Event(
        lastScored = "team2",
        pointsScored = 2,
        team1P = 2,
        team2P = 2,
        matchTime = Duration.ofSeconds(31L),
        matchScore = "2 - 2"
      ),
      Event(
        lastScored = "team2",
        pointsScored = 3,
        team1P = 2,
        team2P = 5,
        matchTime = Duration.ofSeconds(59L),
        matchScore = "2 - 5"
      )
    ))
    val expectedMatch = Match(state = expectedState)
    assert(actualMatch.state.events.size == 3)
    assert(actualMatch.state.events.equals(expectedMatch.state.events))
  }

  test("convertRawToMatchEvents is invoked with malformed raw event 0x2ee74753 should filter out the bad event") {
    val adapter = new RawToMatchEventsAdapter(Stream("0xcc070c6", "0x2ee74753"), new MatchEventRawResponseValidator)

    val actualMatch = adapter.convertRawToMatchEvents
    val expectedState = MatchState(events = Seq(
      Event(
        lastScored = "team2",
        pointsScored = 2,
        team1P = 14,
        team2P = 24,
        matchTime = Duration.ofSeconds(408L),
        matchScore = "14 - 24"
      ))
    )
    val expectedMatch = Match(state = expectedState)
    assert(actualMatch.state.events.size == 1)
    assert(actualMatch.state.events.equals(expectedMatch.state.events))
  }

  test("splitOnIndices method is invoked with a given binary string should split it correctly on the given indices") {
    val adapter = new RawToMatchEventsAdapter(Stream("0xcc070c6", "0x2ee74753"), new MatchEventRawResponseValidator)
    val actual = adapter.splitOnIndices((0, 5), "1001101110110101")
    val expected = "100110"
    assert(actual.equals(expected))
  }

  test("splitOnIndices method is invoked with a given binary string should return empty string for wrong indices") {
    val adapter = new RawToMatchEventsAdapter(Stream("0xcc070c6", "0x2ee74753"), new MatchEventRawResponseValidator)
    val actual = adapter.splitOnIndices((6, 18), "1001101110110101")
    assert(actual.isEmpty)

    val actual2 = adapter.splitOnIndices((5, 2), "1001001001110")
    assert(actual2.isEmpty)
  }

  test("filterOutInvalidEvents is invoked with correct events, should not filter out any event") {
    val adapter = new RawToMatchEventsAdapter(Stream("0x801002", "0xf81016"), new MatchEventRawResponseValidator)
    val events = Seq(
      Event(
        lastScored = "team1",
        pointsScored = 2,
        team1P = 2,
        team2P = 0,
        matchTime = Duration.ofSeconds(16L),
        matchScore = "2 - 0"
      ),
      Event(
        lastScored = "team2",
        pointsScored = 2,
        team1P = 2,
        team2P = 2,
        matchTime = Duration.ofSeconds(31L),
        matchScore = "2 - 2"
      ),
    )
    val actual = adapter.filterOutInvalidEvents(events)
    assert(actual.size == 2)
    assert(actual.equals(events))
  }

  test("filterOutInvalidEvents is invoked with 1 bad event, should filter out 1 of the 2 events passed") {
    val adapter = new RawToMatchEventsAdapter(Stream("0x801002", "0xf81016"), new MatchEventRawResponseValidator)
    val events = Seq(
      Event(
        lastScored = "team1",
        pointsScored = 2,
        team1P = 2,
        team2P = 0,
        matchTime = Duration.ofSeconds(16L),
        matchScore = "2 - 0"
      ),
      Event(
        lastScored = "team2",
        pointsScored = 2,
        team1P = 234,
        team2P = 233,
        matchTime = Duration.ofSeconds(31L),
        matchScore = "234 - 233"
      ),
    )
    val expected = Seq(Event(
      lastScored = "team1",
      pointsScored = 2,
      team1P = 2,
      team2P = 0,
      matchTime = Duration.ofSeconds(16L),
      matchScore = "2 - 0"
    ))
    val actual = adapter.filterOutInvalidEvents(events)
    assert(actual.size == 1)
    assert(actual.equals(expected))
  }

  test("filterOutInvalidEvents is invoked with empty list should return empty") {
    val adapter = new RawToMatchEventsAdapter(Stream("0x801002", "0xf81016"), new MatchEventRawResponseValidator)
    val actual = adapter.filterOutInvalidEvents(Seq.empty)
    assert(actual.isEmpty)
  }
}
