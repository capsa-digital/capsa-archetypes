package digital.capsa.archetypes.command.controller

import java.util.UUID

data class AddBookCommand(

        val volume: String,

        var libraryId: UUID
)
