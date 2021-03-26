package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.BookId
import java.util.UUID

data class CheckInBookCommand(

    val bookId: UUID
)
