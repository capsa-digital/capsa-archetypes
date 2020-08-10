package digital.capsa.it.tests

import assertk.assertThat
import digital.capsa.it.TestContext
import digital.capsa.it.aggregate.Account
import digital.capsa.it.aggregate.account
import digital.capsa.it.dsl.given
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.json.isJsonWhere
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class])
@ExtendWith(DataLoader::class)
class CreateLibraryTest : CapsaApiTestBase() {

    companion object {

        lateinit var demoAccount: Account

        @BeforeAll
        @JvmStatic
        fun createDemoAccount(applicationContext: ApplicationContext) {
            demoAccount = account {
                library {
                    for (i in 1..2) {
                        book { }
                    }
                }
                member {
                    firstName = "John"
                    lastName = "Doe"
                }
                member {
                    phone = "324-222-4567"
                }
                for (i in 1..3) {
                    member { }
                }
            }
            demoAccount.create(TestContext(applicationContext = applicationContext))
        }
    }

    @Test
    fun `verify demo data`() {
        Thread.sleep(2000L)
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort)
        }.on {
            context.httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-all-books.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())

//            assertThat(response.body).isJsonWhere(
//                    ValidationRule("\$.length()", OpType.equal, "{2}")
//            )
        }
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort)
        }.on {
            context.httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-all-members.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())
//            assertThat(response.body).isJsonWhere(
//                    ValidationRule("\$.length()", OpType.equal, "{5}")
//            )
        }
    }
}