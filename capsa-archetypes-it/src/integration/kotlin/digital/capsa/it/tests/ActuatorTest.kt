package digital.capsa.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import digital.capsa.it.TestBase
import digital.capsa.it.httpRequest
import digital.capsa.it.json.isJsonWhere
import digital.capsa.it.validation.OpType
import digital.capsa.it.validation.ValidationRule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Actuator Test")
class ActuatorTest : TestBase() {

    @Test
    fun `call command actuator`() {
        callActuator(appSchema, appHost, appPort, "Capsa Application")
    }

    private fun callActuator(
        schema: String,
        host: String,
        port: String,
        appName: String
    ) {
        httpRequest("/requests/actuator-info.json")
            .withTransformation(
                "$.schema" to schema,
                "$.host" to host,
                "$.port" to port
            )
            .send {
                assertThat(statusCode.value()).isEqualTo(200)
                assertThat(body).isJsonWhere(
                    ValidationRule("$.app.name", OpType.equal, appName),
                    ValidationRule(
                        "$.app.env", OpType.equal,
                        System.getProperty("spring.profiles.active", "")
                    )
                )
            }
    }
}