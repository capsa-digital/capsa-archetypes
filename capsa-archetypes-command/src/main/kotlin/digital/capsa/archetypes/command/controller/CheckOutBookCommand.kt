package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.MemberId

data class CheckOutBookCommand(

    val bookId: BookId,

    val memberId: MemberId,

    val days: Int
)
