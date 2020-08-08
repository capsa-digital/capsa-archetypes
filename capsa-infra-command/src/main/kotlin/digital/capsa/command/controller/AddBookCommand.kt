package digital.capsa.command.controller

import java.util.UUID
import javax.validation.constraints.Email

data class AddBookCommand(

        val volume: String,

        var libraryId: UUID
)
