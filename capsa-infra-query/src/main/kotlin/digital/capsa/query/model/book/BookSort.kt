package digital.capsa.query.model.book

import digital.capsa.core.vocab.BookSortByType

data class BookSort(
        val firstSortBy: BookSortByType?,
        val firstSortDescending: Boolean?,
        val secondSortBy: BookSortByType?,
        val secondSortDescending: Boolean?
)
