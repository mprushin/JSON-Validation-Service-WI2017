import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonschema.main.JsonSchemaFactory

class JsonValidation {

  val objectMapper = new ObjectMapper
  val factory: JsonSchemaFactory = JsonSchemaFactory.byDefault()

  def validate(jsonString: String, schemaString: String): Boolean = {
    return ???
  }


}
