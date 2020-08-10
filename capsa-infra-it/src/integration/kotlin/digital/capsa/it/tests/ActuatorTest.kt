package digital.capsa.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import digital.capsa.it.dsl.given
import digital.capsa.it.event.EventSnooper
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.json.isJsonWhere
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
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to host,
                    "$.port" to port)
        }.on {
            context.httpManager.sendHttpRequest(
                    requestJsonFileName = "/requests/actuator-info.json",
                    transformationData = this)
        }.then { response ->
            assertThat(response.statusCode.value()).isEqualTo(200)
            assertThat(response.body).isJsonWhere(
                    ValidationRule("$.app.name", OpType.equal, appName),
                    ValidationRule("$.app.env", OpType.equal,
                            System.getProperty("spring.profiles.active", ""))
            )
        }
    }
}