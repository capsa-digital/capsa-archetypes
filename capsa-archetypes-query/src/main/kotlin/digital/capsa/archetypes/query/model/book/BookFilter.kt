package digital.capsa.archetypes.query.model.book

import digital.capsa.archetypes.core.vocab.BookStatus

data class BookFilter(
        val leadStatuses: Set<BookStatus>?
)