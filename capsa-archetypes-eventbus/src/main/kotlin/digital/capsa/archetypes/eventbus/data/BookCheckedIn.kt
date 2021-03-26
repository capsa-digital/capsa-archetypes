package digital.capsa.archetypes.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.archetypes.core.aggregates.BookId

@JsonTypeName("BookCheckedIn")
class BookCheckedIn(

        id: BookId

) : BookEventData(
        id = id
)
