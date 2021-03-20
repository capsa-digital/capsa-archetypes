package digital.capsa.query.repo

import digital.capsa.query.model.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BookRepository : JpaRepository<Book, UUID> {
    fun findAllByLibraryId(libraryId: UUID): List<Book>
}
