package digital.capsa.archetypes.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import digital.capsa.archetypes.it.TestBase
import digital.capsa.archetypes.it.aggregate.Account
import digital.capsa.archetypes.it.aggregate.Library
import digital.capsa.archetypes.it.aggregate.Member
import digital.capsa.archetypes.it.aggregate.account
import digital.capsa.archetypes.it.httpRequest
import digital.capsa.it.TestContext
import digital.capsa.it.aggregate.getChild
import digital.capsa.it.json.isJsonWhere
import digital.capsa.it.validation.OpType
import digital.capsa.it.validation.ValidationRule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@DisplayName("Create Library, Members and Books")
class CreateLibraryIt : TestBase() {

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
        TimeUnit.SECONDS.sleep(5)
        val libraryId = demoAccount.getChild<Library>(0).id
        httpRequest("/requests/get-book-list.json")
            .withTransformation(
                "$.schema" to apiSchema,
                "$.host" to apiHost,
                "$.port" to apiPort,
                "$.body.libraryId" to libraryId.toString()
            )
            .send {
                assertThat(statusCode.value()).isEqualTo(200)
                assertEquals(200, statusCode.value())
                assertThat(body).isJsonWhere(
                    ValidationRule("\$.bookList.length()", OpType.equal, 2)
                )
            }
        val memberId = demoAccount.getChild<Member>(0).id
        httpRequest("/requests/get-member-details.json")
            .withTransformation(
                "$.schema" to apiSchema,
                "$.host" to apiHost,
                "$.port" to apiPort,
                "$.path" to "/getMemberDetails/${memberId.toString()}"
            )
            .send {
                assertEquals(200, statusCode.value())
                assertThat(body).isJsonWhere(
                    ValidationRule("$.firstName", OpType.equal, "John")
                )
            }
    }
}