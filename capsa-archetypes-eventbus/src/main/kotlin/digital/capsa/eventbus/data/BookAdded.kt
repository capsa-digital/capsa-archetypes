package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.UUID

@JsonTypeName("BookAdded")
class BookAdded(

        bookId: UUID,

        var libraryId: UUID,

        var volume: String,

        var bookTitle: String,

        var authorName: String,

        var coverURI: String

) : BookEventData(
        bookId = bookId
)
