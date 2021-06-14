package services.client.request

import domain.Event
import validate.ResponseValidator

trait QueryService {

  def getLatestEvent(identifier: String): Event

  def queryForEvents(identifier:String, noOfEvents:Option[Int] = None): Seq[Event]
}
