package digital.capsa.command.controller

import javax.validation.constraints.Email

data class CreateMemberCommand(

        val name: String,

        @Email
        val email: String
)
