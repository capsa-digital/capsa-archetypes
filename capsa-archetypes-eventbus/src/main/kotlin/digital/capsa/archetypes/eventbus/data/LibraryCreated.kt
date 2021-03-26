package digital.capsa.archetypes.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.core.model.Address

@JsonTypeName("LibraryCreated")
class LibraryCreated(

    id: LibraryId,

    var libraryName: String,

    var address: Address

) : LibraryEventData(
        id = id
)
