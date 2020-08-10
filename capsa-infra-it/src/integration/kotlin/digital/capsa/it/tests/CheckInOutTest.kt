package digital.capsa.it.tests

import digital.capsa.core.logger
import digital.capsa.it.TestContext
import digital.capsa.it.aggregate.Account
import digital.capsa.it.aggregate.Library
import digital.capsa.it.aggregate.account
import digital.capsa.it.aggregate.getChild
import digital.capsa.it.dsl.given
import digital.capsa.it.event.EventSnooper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class, EventSnooper::class])
class CheckInOutTest : CapsaApiTestBase() {

    companion object {

        lateinit var testAccount: Account

        @BeforeAll
        @JvmStatic
        fun createTestAccount(applicationContext: ApplicationContext) {
            testAccount = account {
                library {
                    member {
                        firstName = "John"
                        lastName = "Doe"
                    }
                    book { }
                }
            }
            testAccount.create(TestContext(applicationContext = applicationContext))
        }
    }

    @Test
    fun `verify check in events`() {
       val libraryId = testAccount.getChild<Library>(0).id
        logger.info("+++++++++++++++++++ libraryId = " + libraryId)
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
            logger.info("+++++++++++++++++++ " + response.body)
//            assertEquals(200, response.statusCode.value())
//            assertThat(response.body).isJsonWhere(
//                    ValidationRule("\$.length()", OpType.equal, "{2}")
//            )
        }
    }
}