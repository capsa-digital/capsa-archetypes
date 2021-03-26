package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.eventbus.SagaManager
import digital.capsa.archetypes.eventbus.data.BookCheckedOut
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.function.Function

@Service
class CheckOutBookCommandSaga(private val sagaManager: SagaManager) {

    @Bean
    @Qualifier("checkOutBook")
    fun checkOutBook(): Function<CheckOutBookCommand, CommandResponse> {
        return Function { command ->
            command.checkOutBookSaga()
        }
    }

    private fun CheckOutBookCommand.checkOutBookSaga(): CommandResponse {
        val sagaId = sagaManager.runSaga(
            bookCheckedOut()
        )
        return CommandResponse(
            saga = sagaId
        )
    }

    private fun CheckOutBookCommand.bookCheckedOut(): BookCheckedOut {
        return BookCheckedOut(
            id = bookId,
            memberId = memberId,
            checkoutDate = LocalDate.now(),
            returnDate = LocalDate.now().plusDays(days.toLong())
        )
    }
}