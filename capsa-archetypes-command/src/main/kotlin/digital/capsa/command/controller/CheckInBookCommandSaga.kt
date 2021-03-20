package digital.capsa.command.controller

import digital.capsa.eventbus.SagaManager
import digital.capsa.eventbus.data.BookCheckedIn
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
                bookId = bookId
        )
    }
}
