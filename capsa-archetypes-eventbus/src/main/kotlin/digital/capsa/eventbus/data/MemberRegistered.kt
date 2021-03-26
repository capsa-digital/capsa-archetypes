package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.aggregates.MemberId

@JsonTypeName("MemberRegistered")
class MemberRegistered(

    id: MemberId,

    var firstName: String,

    var lastName: String,

    var email: String,

    var phone: String? = null

) : MemberEventData(
    id = id
)
