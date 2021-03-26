package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.eventbus.SagaManager
import digital.capsa.archetypes.eventbus.data.BookCheckedIn
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
class CheckInBookCommandSaga(private val sagaManager: SagaManager) {

    @Bean
    @Qualifier("checkInBook")
    fun checkInBook(): Function<CheckInBookCommand, CommandResponse> {
        return Function { command ->
            command.checkInBookSaga()
        }
    }

    private fun CheckInBookCommand.checkInBookSaga(): CommandResponse {
        val sagaId = sagaManager.runSaga(
                bookCheckedIn()
        )
        return CommandResponse(
                saga = sagaId
        )
    }

    private fun CheckInBookCommand.bookCheckedIn(): BookCheckedIn {
        return BookCheckedIn(
                id = BookId(bookId)
        )
    }
}
