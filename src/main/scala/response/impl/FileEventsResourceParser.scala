package response.impl

import java.io.FileNotFoundException

import response.ResponseParser

import scala.io.Source
import scala.util.Try

class FileEventsResourceParser extends ResponseParser[String, Stream[String]] {

  override def parse(identifier: String): Stream[String] = {
    Try(
      Source.fromFile(identifier).getLines.filter(_.nonEmpty).toStream
    ).getOrElse(
      throw new FileNotFoundException(s"The file at the specified directory $identifier was not found")
    )
  }
}
