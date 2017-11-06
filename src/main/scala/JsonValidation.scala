import java.util

import scala.collection.JavaConverters._
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonschema.core.report.{ProcessingMessage, ProcessingReport}
import com.github.fge.jsonschema.main.JsonSchemaFactory
import io.circe.{Json, Printer}

object JsonValidation {

  val objectMapper = new ObjectMapper
  val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()

  /**
    * Validates json to schema
    *
    * @param json
    * @param schema
    * @return Pair of (successful flag, error message is unsuccesfull)
    */
  def validate(json: Json, schema: Json): (Boolean, String) = {
    val jsonJson = objectMapper.readTree(json.pretty(Printer.noSpaces))
    val schemaJson = objectMapper.readTree(schema.pretty(Printer.noSpaces))
    val schemaObject = factory.getJsonSchema(schemaJson)
    val report = schemaObject.validate(jsonJson)
    if (report.isSuccess) {
      (true, "")
    } else {
      (false, getMessage(report.iterator()))
    }
  }

  /**
    * Returns message from the validation outpur
    * @param iterator - Java.util.Iterator[ProcessingMessage]
    * @return - Message joined from the input iterator
    */
  def getMessage(iterator: util.Iterator[ProcessingMessage]): String = {
    iterator.asScala.map((message) => message.getMessage).reduce((m1, m2) => m1 + ", " + m2)
  }
}
