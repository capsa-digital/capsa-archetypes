package digital.capsa.query.query.v1.dto

import java.util.UUID

data class MemberInfoDTO(
        var memberId: UUID,
        var firstName: String,
        var lastName: String,
        var email: String,
        var phone: String? = null
)
