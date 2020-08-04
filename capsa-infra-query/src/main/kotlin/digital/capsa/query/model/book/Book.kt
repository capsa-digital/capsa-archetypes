package digital.capsa.query.model.book

import digital.capsa.core.vocab.BookStatus
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "book")
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
data class Book(

        @Id
        var bookId: UUID,

        var volume: String,

        var bookTitle: String,

        var authorName: String,

        var coverURI: String,

        var memberId: UUID? = null,

        @Enumerated(EnumType.STRING)
        var bookStatus: BookStatus
)
