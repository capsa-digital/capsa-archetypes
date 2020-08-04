package digital.capsa.command.controller

import java.util.UUID

data class CheckOutBookCommand(

        val bookId: UUID,

        val memberId: UUID,

        val days: Int
)
