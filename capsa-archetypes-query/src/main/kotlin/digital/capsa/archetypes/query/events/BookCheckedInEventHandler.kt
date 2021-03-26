package digital.capsa.archetypes.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.archetypes.eventbus.Event
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.eventbus.data.BookCheckedIn
import digital.capsa.archetypes.query.services.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
open class BookCheckedInEventHandler(private var bookService: BookService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='BookCheckedIn'")
    fun handleEvent(event: Event<BookCheckedIn>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        bookService.checkInBook(event.data.id)
    }
}
