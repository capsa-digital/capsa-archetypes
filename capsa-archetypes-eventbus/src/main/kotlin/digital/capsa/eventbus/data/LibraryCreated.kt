package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.model.Address
import java.util.UUID

@JsonTypeName("LibraryCreated")
class LibraryCreated(

        libraryId: UUID,

        var libraryName: String,

        var address: Address

) : LibraryEventData(
        libraryId = libraryId
)
