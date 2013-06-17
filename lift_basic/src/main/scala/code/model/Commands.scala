package code
package model

import net.liftweb._
import util._
import Helpers._
import json._
import com.github.nscala_time.time.Imports._
import net.liftweb.json.JsonDSL._

object Commands {
  private implicit val formats =
    json.DefaultFormats + DateTimeSerializer + new TimeSpanSerializer

  def startRun(name: String, json: JValue)= {
    val now: com.github.nscala_time.time.Imports.DateTime = DateTime.now
    val extraFields: JValue = ("runId" -> 3) ~  ("testing" -> 5)  ~ ("startTime" -> now)
    json merge extraFields
  }
}

object DateTimeSerializer extends Serializer[com.github.nscala_time.time.Imports.DateTime] {
  private val Class = classOf[com.github.nscala_time.time.Imports.DateTime]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), com.github.nscala_time.time.Imports.DateTime] = {
    case (TypeInfo(Class, _), json) => json match  {
      case JInt(millis) => new com.github.nscala_time.time.Imports.DateTime(millis.longValue)
      case x => throw new MappingException("Can't convert " + x + " to com.github.nscala_time.time.Imports.DateTime")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case t: com.github.nscala_time.time.Imports.DateTime => JInt(t.getMillis)
  }
}

// based on https://groups.google.com/d/topic/liftweb/C9PUhGSO8oQ/discussion
class TimeSpanSerializer extends CustomSerializer[com.github.nscala_time.time.Imports.DateTime](format => (
  {
    case JInt(millis) => new com.github.nscala_time.time.Imports.DateTime(millis.longValue)
  },
  {
    case t: com.github.nscala_time.time.Imports.DateTime => JInt(t.getMillis)
  }
))

