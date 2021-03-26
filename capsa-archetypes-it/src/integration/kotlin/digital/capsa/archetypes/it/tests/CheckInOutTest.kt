package digital.capsa.archetypes.it.tests

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import digital.capsa.archetypes.eventbus.data.BookCheckedOut
import digital.capsa.archetypes.it.TestBase
import digital.capsa.it.TestContext
import digital.capsa.archetypes.it.aggregate.Account
import digital.capsa.archetypes.it.aggregate.Book
import digital.capsa.archetypes.it.aggregate.Library
import digital.capsa.archetypes.it.aggregate.Member
import digital.capsa.archetypes.it.aggregate.account
import digital.capsa.it.aggregate.getChild
import digital.capsa.archetypes.it.event.EventSnooper
import digital.capsa.archetypes.it.httpRequest
import digital.capsa.archetypes.it.runner.TabularSource
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import java.time.LocalDate
import kotlin.test.assertEquals

@DisplayName("Verify Book CheckIn/CheckOut functionality")
class CheckInOutTest : TestBase() {

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
    @TabularSource(
        """
           | Five days | 5  |
           | Ten days  | 10 |
        """
    )
    fun `verify check in events`(description: String, days: Int) {
        val bookId = testAccount.getChild<Library>(0).getChild<Book>(0).id
        val memberId = testAccount.getChild<Member>(0).id
        Thread.sleep(2000L)
        eventSnooper.clear()
        httpRequest("/requests/check-out-book.json")
            .withTransformation(
                "$.schema" to appSchema,
                "$.host" to appHost,
                "$.port" to appPort,
                "$.body.bookId" to bookId.toString(),
                "$.body.memberId" to memberId.toString(),
                "$.body.days" to days
            )
            .send {
                Thread.sleep(2000L)
                assertEquals(200, statusCode.value())
                val eventData = eventSnooper.getEvents()[0].data
                assertThat(eventData).isInstanceOf(BookCheckedOut::class).also {
                    with(eventData as BookCheckedOut) {
                        assertThat(bookId).isEqualTo(bookId)
                        assertThat(memberId).isEqualTo(memberId)
                        assertThat(checkoutDate).isEqualTo(LocalDate.now())
                        assertThat(returnDate).isEqualTo(LocalDate.now().plusDays(days.toLong()))
                    }
                }
            }
    }
}