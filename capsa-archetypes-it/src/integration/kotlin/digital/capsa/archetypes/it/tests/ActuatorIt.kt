package digital.capsa.archetypes.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import digital.capsa.archetypes.it.TestBase
import digital.capsa.archetypes.it.httpRequest
import digital.capsa.it.json.isJsonWhere
import digital.capsa.it.validation.OpType
import digital.capsa.it.validation.ValidationRule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Actuator Test")
class ActuatorIt : TestBase() {

    @Test
    fun `call command actuator`() {
        httpRequest("/requests/actuator-info.json")
            .withTransformation(
                "$.schema" to apiSchema,
                "$.host" to apiHost,
                "$.port" to apiPort
            )
            .send {
                assertThat(statusCode.value()).isEqualTo(200)
                assertThat(body).isJsonWhere(
                    ValidationRule("$.app.name", OpType.equal, "Capsa Application"),
                    ValidationRule(
                        "$.app.env", OpType.equal,
                        System.getProperty("spring.profiles.active", "")
                    )
                )
            }
    }
}