package digital.capsa.it.tests

import assertk.assertThat
import digital.capsa.it.TestContext
import digital.capsa.it.aggregate.Account
import digital.capsa.it.aggregate.Library
import digital.capsa.it.aggregate.Member
import digital.capsa.it.aggregate.account
import digital.capsa.it.aggregate.getChild
import digital.capsa.it.dsl.given
import digital.capsa.it.json.OpType
import digital.capsa.it.json.ValidationRule
import digital.capsa.it.json.isJsonWhere
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
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
@DisplayName("Create Library, Members and Books")
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
        val libraryId = demoAccount.getChild<Library>(0).id
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort,
                    "$.body.libraryId" to libraryId.toString())
        }.on {
            context.httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-book-list.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())
            assertThat(response.body).isJsonWhere(
                    ValidationRule("\$.bookList.length()", OpType.equal, "{2}")
            )
        }
        val memberId = demoAccount.getChild<Member>(0).id
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to queryHost,
                    "$.port" to queryPort,
                    "$.path" to "/getMemberDetails/${memberId.toString()}")
        }.on {
            context.httpManager.sendHttpRequest(requestJsonFileName = "/requests/get-member-details.json",
                    transformationData = this)
        }.then { response ->
            assertEquals(200, response.statusCode.value())
            assertThat(response.body).isJsonWhere(
                    ValidationRule("$.firstName", OpType.equal, "John")
            )
        }
    }
}