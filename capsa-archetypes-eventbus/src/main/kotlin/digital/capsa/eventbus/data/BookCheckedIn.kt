package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.aggregates.BookId
import java.util.UUID

@JsonTypeName("BookCheckedIn")
class BookCheckedIn(

        id: BookId

) : BookEventData(
        id = id
)
