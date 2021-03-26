package digital.capsa.archetypes.query.model.book

import digital.capsa.archetypes.core.vocab.BookSortByType

data class BookSort(
    val firstSortBy: BookSortByType?,
    val firstSortDescending: Boolean?,
    val secondSortBy: BookSortByType?,
    val secondSortDescending: Boolean?
)
