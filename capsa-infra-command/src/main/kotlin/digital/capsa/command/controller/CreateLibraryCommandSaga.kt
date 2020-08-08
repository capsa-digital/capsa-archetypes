package digital.capsa.command.controller

import digital.capsa.core.vocab.AggregateType
import digital.capsa.eventbus.SagaManager
import digital.capsa.eventbus.data.LibraryCreated
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function

@Service
class CreateLibraryCommandSaga(private val sagaManager: SagaManager) {

    @Bean
    @Qualifier("createLibrary")
    fun createLibrary(): Function<CreateLibraryCommand, CommandResponse> {
        return Function { command ->
            command.createLibrarySaga()
        }
    }

    private fun CreateLibraryCommand.createLibrarySaga(): CommandResponse {
        val libraryId: UUID = UUID.randomUUID()
        val sagaId = sagaManager.runSaga(
                libraryCreated(libraryId = libraryId)
        )
        return CommandResponse(
                saga = sagaId,
                ids = mapOf(
                        AggregateType.library to libraryId
                )
        )
    }

    private fun CreateLibraryCommand.libraryCreated(libraryId: UUID) = LibraryCreated(
            libraryId = libraryId,
            libraryName = libraryName,
            address = address
    )
}
