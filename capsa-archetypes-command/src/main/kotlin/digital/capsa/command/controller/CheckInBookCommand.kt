package digital.capsa.command.controller

import digital.capsa.core.aggregates.BookId

data class CheckInBookCommand(

    val bookId: BookId
)
