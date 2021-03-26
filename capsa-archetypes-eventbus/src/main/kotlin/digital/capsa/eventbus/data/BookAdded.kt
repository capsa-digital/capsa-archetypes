package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.LibraryId
import java.util.UUID

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
