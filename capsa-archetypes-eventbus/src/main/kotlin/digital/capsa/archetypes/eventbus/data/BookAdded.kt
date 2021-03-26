package digital.capsa.archetypes.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.LibraryId

@JsonTypeName("BookAdded")
class BookAdded(

    id: BookId,

    var libraryId: LibraryId,

    var volume: String,

    var bookTitle: String,

    var authorName: String,

    var coverURI: String

) : BookEventData(
        id = id
)
