package digital.capsa.archetypes.command.controller

import javax.validation.constraints.Email

data class RegisterMemberCommand(

        var firstName: String,

        var lastName: String,

        @Email
        val email: String,

        var phone: String? = null
)
