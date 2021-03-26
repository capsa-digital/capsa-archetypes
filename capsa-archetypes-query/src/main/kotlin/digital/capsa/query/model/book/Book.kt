package digital.capsa.query.model.book

import digital.capsa.core.aggregates.Aggregate
import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.LibraryId
import digital.capsa.core.aggregates.MemberId
import digital.capsa.core.vocab.BookStatus
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "book")
@Table(name = "book")
data class Book(

    @Id
    override var id: BookId,

    var libraryId: LibraryId,

    var volume: String,

    var bookTitle: String,

    var authorName: String,

    @Column(length = 1000)
    var coverURI: String,

    var memberId: MemberId? = null,

    @Enumerated(EnumType.STRING)
    var bookStatus: BookStatus,

    var checkoutDate: LocalDate? = null,

    var returnDate: LocalDate? = null
) : Aggregate<BookId>()
