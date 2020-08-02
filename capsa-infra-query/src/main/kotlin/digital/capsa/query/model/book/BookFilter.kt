package digital.capsa.query.model.book

import digital.capsa.core.vocab.BookStatus

data class BookFilter(
        val leadStatuses: Set<BookStatus>?
)