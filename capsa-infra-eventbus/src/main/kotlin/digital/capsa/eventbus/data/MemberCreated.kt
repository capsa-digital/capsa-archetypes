package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.UUID

@JsonTypeName("MemberCreated")
class MemberCreated(

        memberId: UUID,

        var firstName: String,

        var lastName: String,

        var email: String

) : MemberEventData(
        memberId = memberId
)
