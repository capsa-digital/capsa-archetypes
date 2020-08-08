package digital.capsa.it.tests

import digital.capsa.it.json.JsonPathValidator
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.runner.HttpManager
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals


@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.properties"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class])
class ActuatorTest {

    @Autowired
    lateinit var httpManager: HttpManager

    @Test
    fun callActuator(@Value("\${metrofox.host}") host: String,
                     @Value("\${metrofox.schema}") schema: String,
                     @Value("\${metrofox.port}") port: String) {
        val response = httpManager.sendHttpRequest(requestJsonFileName = "/requests/actuator-info.json",
                transformationData = mapOf(
                        "$.schema" to schema,
                        "$.host" to host,
                        "$.port" to port,
                        "$.path" to "/api/actuator/info"))
        assertEquals(200, response.statusCode.value())

        val profiles = System.getProperty("spring.profiles.active", "")

        JsonPathValidator.assertJson(response.body.toString(), listOf(
                ValidationRule("\$.app.name", OpType.equal, "Metrofox Application"),
                ValidationRule("\$.app.env", OpType.equal, profiles)
        ))
    }
}