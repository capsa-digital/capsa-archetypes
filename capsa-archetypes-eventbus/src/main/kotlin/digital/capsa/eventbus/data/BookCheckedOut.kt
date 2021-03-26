package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.MemberId
import java.time.LocalDate
import java.util.UUID

@JsonTypeName("BookCheckedOut")
class BookCheckedOut(

        id: BookId,

        var memberId: MemberId,

        var checkoutDate: LocalDate,

        var returnDate: LocalDate

) : BookEventData(
        id = id
)
