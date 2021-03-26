package digital.capsa.query.repo

import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.LibraryId
import digital.capsa.query.model.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, BookId> {
    fun findAllByLibraryId(libraryId: LibraryId): List<Book>
}
