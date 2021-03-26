package digital.capsa.archetypes.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.MemberId
import java.time.LocalDate

@JsonTypeName("BookCheckedOut")
class BookCheckedOut(

    id: BookId,

    var memberId: MemberId,

    var checkoutDate: LocalDate,

    var returnDate: LocalDate

) : BookEventData(
        id = id
)
