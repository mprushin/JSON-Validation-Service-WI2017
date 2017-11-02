import java.util
import scala.collection.JavaConverters._

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonschema.core.report.{ProcessingMessage, ProcessingReport}
import com.github.fge.jsonschema.main.JsonSchemaFactory

class JsonValidation {

  val objectMapper = new ObjectMapper
  val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()

  /**
    * Validates json to schema
    *
    * @param jsonString
    * @param schemaString
    * @return Pair of (successful flag, error message is unsuccesfull)
    */
  def validate(jsonString: String, schemaString: String): (Boolean, String) = {
    val json = objectMapper.readTree(jsonString)
    val schemaJson = objectMapper.readTree(schemaString)
    val schema = factory.getJsonSchema(schemaJson)
    val report = schema.validate(json)
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
