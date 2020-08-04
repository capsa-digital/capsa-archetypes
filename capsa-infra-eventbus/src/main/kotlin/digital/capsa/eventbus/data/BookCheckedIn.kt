package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.UUID

@JsonTypeName("BookCheckedIn")
class BookCheckedIn(

        bookId: UUID

) : BookEventData(
        bookId = bookId
)
