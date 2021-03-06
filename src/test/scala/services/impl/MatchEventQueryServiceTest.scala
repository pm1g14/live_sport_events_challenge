package services.impl

import java.time.Duration

import domain.Event
import org.scalatest.FunSuite
import response.impl.FileEventsResourceParser
import services.client.request.impl.MatchEventQueryService
import validate.impl.MatchEventRawResponseValidator

class MatchEventQueryServiceTest extends FunSuite {

  /*
  * Tests in this suite assume we're reading from the sample files. In real world, an API endpoint could be built around
  * client requests.
   */
  private val validator = new MatchEventRawResponseValidator
  private val resourceParser = new FileEventsResourceParser()

  test("getLatestEvent is invoked with a specific match id should return the latest match event") {
    val matchId = "src/test/resources/sample1.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLastEvent = Event(
      lastScored = "team1",
      pointsScored = 2,
      matchTime = Duration.ofSeconds(598L),
      team1P = 27,
      team2P = 29,
      matchScore = "27 - 29"
    )
    assert(service.getLatestEvent(matchId).equals(expectedLastEvent))
  }

  test("queryForEvents is invoked with a specific no of events (3) should return last 3 events") {
    val matchId = "src/test/resources/sample1.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLast3Events = Stream(
      Event(
        lastScored = "team2",
        pointsScored = 1,
        matchTime = Duration.ofSeconds(559L),
        team1P = 23,
        team2P = 29,
        matchScore = "23 - 29"
      ),
      Event(
        lastScored = "team1",
        pointsScored = 2,
        matchTime = Duration.ofSeconds(581L),
        team1P = 25,
        team2P = 29,
        matchScore = "25 - 29"
      ),
      Event(
        lastScored = "team1",
        pointsScored = 2,
        matchTime = Duration.ofSeconds(598L),
        team1P = 27,
        team2P = 29,
        matchScore = "27 - 29"
      )
    )
    assert(expectedLast3Events.equals(service.queryForEvents(matchId, Some(3))))
  }

  test("queryForEvents is invoked with no number of events, should return all events in file") {
    val matchId = "src/test/resources/sample1.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    assert(service.queryForEvents(matchId).size == 28)
  }

  test("queryForEvents is invoked for 2nd sample, should return all valid events (26)") {
    val matchId = "src/test/resources/sample2.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    assert(service.queryForEvents(matchId).size == 26)
  }

  test("getLatestEvent is invoked for 2nd sample, should return last event if given 1") {
    val matchId = "src/test/resources/sample2.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLastEvent = Event(
      lastScored = "team1",
      pointsScored = 2,
      matchTime = Duration.ofSeconds(579L),
      team1P = 25,
      team2P = 29,
      matchScore = "25 - 29"
    )
    assert(service.getLatestEvent(matchId).equals(expectedLastEvent))
  }

  test("invoking getLatestEvent for a big event stream should return with the last valid element") {
    val matchId = "src/test/resources/sample3.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLastEvent = Event(
      lastScored = "team1",
      pointsScored = 2,
      matchTime = Duration.ofSeconds(579L),
      team1P = 25,
      team2P = 29,
      matchScore = "25 - 29"
    )
    assert(service.getLatestEvent(matchId).equals(expectedLastEvent))
  }

  test("invoking getLatestEvent for an event with empty values in between events should return the last valid event") {
    val matchId = "src/test/resources/sample4.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLastEvent = Event(
      lastScored = "team1",
      pointsScored = 2,
      matchTime = Duration.ofSeconds(598L),
      team1P = 27,
      team2P = 29,
      matchScore = "27 - 29"
    )
    assert(service.getLatestEvent(matchId).equals(expectedLastEvent))
  }

  test("invoking queryForEvents for the last 2 values on a file with empty lines, should return the correct values") {
    val matchId = "src/test/resources/sample4.txt"
    val service = new MatchEventQueryService(resourceParser, validator)
    val expectedLastEvent = Stream(
      Event(
        lastScored = "team1",
        pointsScored = 2,
        matchTime = Duration.ofSeconds(581L),
        team1P = 25,
        team2P = 29,
        matchScore = "25 - 29"
      ),
      Event(
        lastScored = "team1",
        pointsScored = 2,
        matchTime = Duration.ofSeconds(598L),
        team1P = 27,
        team2P = 29,
        matchScore = "27 - 29"
    ))
    assert(service.queryForEvents(matchId, Some(2)).equals(expectedLastEvent))
  }

  test("invoking queryForEvents for the last 2 values on an empty file should return empty") {
    val matchId = "src/test/resources/sample5.txt"
    val service = new MatchEventQueryService(resourceParser, validator)

    assert(service.queryForEvents(matchId, Some(2)).isEmpty)
  }

}
