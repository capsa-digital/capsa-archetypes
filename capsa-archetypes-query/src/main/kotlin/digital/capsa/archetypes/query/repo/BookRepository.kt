package digital.capsa.archetypes.query.repo

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.query.model.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, BookId> {
    fun findAllByLibraryId(libraryId: LibraryId): List<Book>
}
