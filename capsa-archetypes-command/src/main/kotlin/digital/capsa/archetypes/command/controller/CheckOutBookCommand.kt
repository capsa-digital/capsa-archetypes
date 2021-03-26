package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.MemberId
import java.util.UUID

data class CheckOutBookCommand(

    val bookId: UUID,

    val memberId: UUID,

    val days: Int
)
