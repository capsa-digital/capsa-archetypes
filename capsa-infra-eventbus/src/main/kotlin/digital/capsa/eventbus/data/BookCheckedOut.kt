package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDate
import java.util.UUID

@JsonTypeName("BookCheckedOut")
class BookCheckedOut(

        bookId: UUID,

        var memberId: UUID,

        var checkoutDate: LocalDate,

        var returnDate: LocalDate

) : BookEventData(
        bookId = bookId
)
