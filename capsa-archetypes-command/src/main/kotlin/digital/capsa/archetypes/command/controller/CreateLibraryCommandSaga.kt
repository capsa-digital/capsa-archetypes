package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.eventbus.SagaManager
import digital.capsa.archetypes.eventbus.data.LibraryCreated
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
        val libraryId = LibraryId(UUID.randomUUID())
        val sagaId = sagaManager.runSaga(
            libraryCreated(libraryId = libraryId)
        )
        return CommandResponse(
            saga = sagaId,
            ids = listOf(libraryId)
        )
    }

    private fun CreateLibraryCommand.libraryCreated(libraryId: LibraryId) = LibraryCreated(
        id = libraryId,
        libraryName = libraryName,
        address = address
    )
}
