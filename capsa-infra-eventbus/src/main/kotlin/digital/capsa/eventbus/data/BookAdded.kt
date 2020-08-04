package digital.capsa.eventbus.data

import com.fasterxml.jackson.annotation.JsonTypeName
import java.util.UUID

@JsonTypeName("BookAdded")
class BookAdded(

        bookId: UUID,

        var volume: String,

        var bookName: String,

        var authorName: String,

        var coverURI: String

) : BookEventData(
        bookId = bookId
)
