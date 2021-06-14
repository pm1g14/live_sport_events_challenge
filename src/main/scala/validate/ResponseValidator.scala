package validate

import domain.Event

trait ResponseValidator {

  def validate(event: Event, lastEvent: Option[Event]): Boolean

}
