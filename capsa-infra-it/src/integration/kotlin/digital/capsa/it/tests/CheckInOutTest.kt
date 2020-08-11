package digital.capsa.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import digital.capsa.eventbus.data.BookCheckedOut
import digital.capsa.it.TestContext
import digital.capsa.it.aggregate.Account
import digital.capsa.it.aggregate.Book
import digital.capsa.it.aggregate.Library
import digital.capsa.it.aggregate.Member
import digital.capsa.it.aggregate.account
import digital.capsa.it.aggregate.getChild
import digital.capsa.it.dsl.given
import digital.capsa.it.event.EventSnooper
import digital.capsa.it.runner.TabularSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import kotlin.test.assertEquals

@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class, EventSnooper::class])
@DisplayName("Verify Book CheckIn/CheckOut functionality")
class CheckInOutTest : CapsaApiTestBase() {

    companion object {

        lateinit var testAccount: Account

        @BeforeAll
        @JvmStatic
        fun createTestAccount(applicationContext: ApplicationContext) {
            testAccount = account {
                library {
                    book { }
                }
                member {
                    firstName = "Jane"
                    lastName = "Doe"
                }
            }
            testAccount.create(TestContext(applicationContext = applicationContext))
        }
    }

    @Autowired
    lateinit var eventSnooper: EventSnooper

    @ParameterizedTest
    @TabularSource("""
           | Five days | 5  |
           | Ten days  | 10 |
        """)
    fun `verify check in events`(description: String, days: Int) {
        val bookId = testAccount.getChild<Library>(0).getChild<Book>(0).id
        val memberId = testAccount.getChild<Member>(0).id
        given {
            mapOf(
                    "$.schema" to schema,
                    "$.host" to commandHost,
                    "$.port" to commandPort,
                    "$.body.bookId" to bookId.toString(),
                    "$.body.memberId" to memberId.toString(),
                    "$.body.days" to days
            )
        }.on {
            Thread.sleep(2000L)
            eventSnooper.clear()
            context.httpManager.sendHttpRequest(requestJsonFileName = "/requests/check-out-book.json",
                    transformationData = this)
        }.then { response ->
            Thread.sleep(2000L)
            assertEquals(200, response.statusCode.value())
            val eventData = eventSnooper.getEvents()[0].data
            assertThat(eventData).isInstanceOf(BookCheckedOut::class).also {
                eventData as BookCheckedOut
                assertThat(eventData.bookId).isEqualTo(bookId)
                assertThat(eventData.memberId).isEqualTo(memberId)
                assertThat(eventData.checkoutDate).isEqualTo(LocalDate.now())
                assertThat(eventData.returnDate).isEqualTo(LocalDate.now().plusDays(days.toLong()))
            }
        }
    }
}