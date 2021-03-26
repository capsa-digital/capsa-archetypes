package digital.capsa.archetypes.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.archetypes.eventbus.Event
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.eventbus.data.BookCheckedOut
import digital.capsa.archetypes.query.services.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
open class BookCheckedOutEventHandler(private var bookService: BookService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='BookCheckedOut'")
    fun handleEvent(event: Event<BookCheckedOut>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        bookService.checkOutBook(
                bookId = event.data.id,
                memberId = event.data.memberId,
                checkoutDate = event.data.checkoutDate,
                returnDate = event.data.returnDate
        )
    }
}
