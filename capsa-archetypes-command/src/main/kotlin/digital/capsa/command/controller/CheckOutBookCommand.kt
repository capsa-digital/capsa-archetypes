package digital.capsa.command.controller

import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.MemberId

data class CheckOutBookCommand(

    val bookId: BookId,

    val memberId: MemberId,

    val days: Int
)
