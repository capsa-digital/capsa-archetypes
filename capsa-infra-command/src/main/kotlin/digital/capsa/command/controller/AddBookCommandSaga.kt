package digital.capsa.command.controller

import digital.capsa.core.vocab.AggregateType
import digital.capsa.eventbus.SagaManager
import digital.capsa.eventbus.data.BookAdded
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function

@Service
class AddBookCommandSaga(private val sagaManager: SagaManager) {

    @Bean
    @Qualifier("addBook")
    fun addBook(): Function<AddBookCommand, CommandResponse> {
        return Function { command ->
            command.addBookSaga()
        }
    }

    private fun AddBookCommand.addBookSaga(): CommandResponse {
        val bookId: UUID = UUID.randomUUID()
        val sagaId = sagaManager.runSaga(
                bookAdded(bookId = bookId, volume = volume)
        )
        return CommandResponse(
                saga = sagaId,
                ids = mapOf(
                        AggregateType.book to bookId
                )
        )
    }

    private fun AddBookCommand.bookAdded(bookId: UUID, volume: String): BookAdded {

        //TODO call https://www.googleapis.com/books/v1/volumes/RDl4BgAAQBAJ

        val bookName = ""
        val authorName = ""
        val coverURI = ""

        return BookAdded(
                bookId = bookId,
                volume = volume,
                bookName = bookName,
                authorName = authorName,
                coverURI = coverURI
        )
    }
}
