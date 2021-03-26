package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.BookId

data class CheckInBookCommand(

    val bookId: BookId
)
