package digital.capsa.it.tests

import assertk.assertThat
import digital.capsa.it.dsl.given
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.json.isJsonWhere
import digital.capsa.it.runner.HttpManager
import org.junit.Assume
import org.junit.BeforeClass
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
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
@ExtendWith(DataLoader::class)
class PreloadedDataTest : CapsaApiTestBase() {

    @Autowired
    lateinit var httpManager: HttpManager

    @BeforeClass
    fun disableTestsOnProd() {
        val profilesFromConsole = System.getProperty("spring.profiles.active", "")
        Assume.assumeFalse(profilesFromConsole.contains("prod"))
    }

    @Test
    fun `verify preloaded data`() {
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort)
        }.on {
            httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-all-books.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())
            assertThat(response.body).isJsonWhere(
                    ValidationRule("\$.length()", OpType.equal, "{2}")
            )
        }
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort)
        }.on {
            httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-all-members.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())
            assertThat(response.body).isJsonWhere(
                    ValidationRule("\$.length()", OpType.equal, "{5}")
            )
        }
    }
}