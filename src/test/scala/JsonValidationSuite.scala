import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JsonValidationSuite
  extends JsonValidationTest
    with JsonUtilsTest
    with JsonStorageTest
    with EndpointsTest
