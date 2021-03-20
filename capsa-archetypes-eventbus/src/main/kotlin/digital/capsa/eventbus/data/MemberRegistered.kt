package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.UUID

@JsonTypeName("MemberRegistered")
class MemberRegistered(

        memberId: UUID,

        var firstName: String,

        var lastName: String,

        var email: String,

        var phone: String? = null

) : MemberEventData(
        memberId = memberId
)
