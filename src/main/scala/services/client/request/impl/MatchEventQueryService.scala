package services.client.request.impl

import adapters.impl.RawToMatchEventsAdapter
import domain.Event
import response.ResponseParser
import services.client.request.QueryService
import validate.impl.MatchEventRawResponseValidator

class MatchEventQueryService(responseParser: ResponseParser[String, Stream[String]], validator: MatchEventRawResponseValidator) extends QueryService {

  override def getLatestEvent(id: String): Event = {
    val adapter = new RawToMatchEventsAdapter(responseParser.parse(id), validator)
    adapter.convertRawToMatchEvents.state.events.last
  }


  override def queryForEvents(id: String, noOfEvents: Option[Int]): Stream[Event] = {
    val adapter = new RawToMatchEventsAdapter(responseParser.parse(id), validator)
    noOfEvents match {
      case Some(value) => adapter.convertRawToMatchEvents.state.events.toStream.takeRight(value)
      case _           => adapter.convertRawToMatchEvents.state.events.toStream
    }
  }
}
