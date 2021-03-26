package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.aggregates.LibraryId
import digital.capsa.core.model.Address
import java.util.UUID

@JsonTypeName("LibraryCreated")
class LibraryCreated(

        id: LibraryId,

        var libraryName: String,

        var address: Address

) : LibraryEventData(
        id = id
)
