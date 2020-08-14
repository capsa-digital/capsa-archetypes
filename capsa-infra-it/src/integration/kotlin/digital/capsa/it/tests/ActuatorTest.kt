package digital.capsa.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.json.isJsonWhere
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class])
@DisplayName("Actuator Test")
class ActuatorTest : CapsaApiTestBase() {

    @Test
    fun `call command actuator`() {
        callActuator(schema, commandHost, commandPort, "Capsa Command Application")
    }

    @Test
    fun `call query actuator`() {
        callActuator(schema, queryHost, queryPort, "Capsa Query Application")
    }

    private fun callActuator(schema: String,
                             host: String,
                             port: String,
                             appName: String) {
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
                            ValidationRule("$.app.env", OpType.equal,
                                    System.getProperty("spring.profiles.active", "")))
                }
    }
}