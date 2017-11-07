object JsonResponseModels {
  case class JsonOperationResponse(action: String, id: String, status: String, message: Option[String])


  def uploadedSuccessfully = JsonOperationResponse("uploadSchema", "config-schema", "success", None)//.asJson.pretty(Printer.spaces2.copy(dropNullValues = true))
  def uploadError = JsonOperationResponse("uploadSchema", "config-schema", "error", Some("Invalid JSON"))//.asJson.pretty(Printer.spaces2)

  def validatedSuccessfully = JsonOperationResponse("validateDocument", "config-schema", "success", None)//.asJson.pretty(Printer.spaces2.copy(dropNullValues = true))
  def validateError(message: String) = JsonOperationResponse("validateDocument", "config-schema", "error", Some(message))//.asJson.pretty(Printer.spaces2.copy(dropNullValues = true))
}