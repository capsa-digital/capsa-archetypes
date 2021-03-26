package digital.capsa.archetypes.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.archetypes.core.vocab.BookStatus
import digital.capsa.archetypes.eventbus.Event
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.eventbus.data.BookAdded
import digital.capsa.archetypes.query.model.book.Book
import digital.capsa.archetypes.query.services.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
open class BookAddedEventHandler(private var bookService: BookService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='BookAdded'")
    fun handleEvent(event: Event<BookAdded>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        bookService.addBook(event.data.transform())
    }

    private fun BookAdded.transform(): Book = Book(
        id = id,
        libraryId = libraryId,
        volume = volume,
        bookTitle = bookTitle,
        authorName = authorName,
        coverURI = coverURI,
        bookStatus = BookStatus.available
    )
}
