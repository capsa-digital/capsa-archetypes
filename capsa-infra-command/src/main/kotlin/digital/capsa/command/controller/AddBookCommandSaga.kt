package digital.capsa.command.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.vocab.AggregateType
import digital.capsa.eventbus.SagaManager
import digital.capsa.eventbus.data.BookAdded
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
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
        val restTemplate = RestTemplate()
        val url = "https://www.googleapis.com/books/v1/volumes/$volume"
        val response = restTemplate.getForEntity(url, String::class.java)

        val mapper = ObjectMapper()
        val root: JsonNode = mapper.readTree(response.body)
        return BookAdded(
                bookId = bookId,
                libraryId = libraryId,
                volume = volume,
                bookTitle = root.path("volumeInfo").path("title").textValue(),
                authorName = root.path("volumeInfo").path("authors")[0].textValue(),
                coverURI = root.path("volumeInfo").path("imageLinks").path("thumbnail").textValue()
        )
    }
}
