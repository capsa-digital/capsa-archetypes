package digital.capsa.archetypes.query.services

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.core.aggregates.MemberId
import digital.capsa.archetypes.core.vocab.BookStatus
import digital.capsa.archetypes.query.model.book.Book
import digital.capsa.archetypes.query.repo.BookRepository
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class BookService(private val repository: BookRepository) {

    fun getBook(bookId: BookId): Book {
        return repository.getOne(bookId)
    }

    fun getBookList(libraryId: LibraryId): List<Book> {
        return repository.findAllByLibraryId(libraryId)
    }

    //TODO
//    fun getBookList(agencyId: UUID, pageNumber: Int, pageSize: Int, search: String?, filter: BookFilter, sort: BookSort): List<Book> {
//        return repository.findAllLeadsByCriteria(agencyId, pageNumber, pageSize, search, filter, sort)
//    }

    fun addBook(book: Book) {
        if (repository.existsById(book.id)) {
            throw Error("Book with ${book.id} already exist")
        }
        repository.save(book)
    }

    fun checkInBook(bookId: BookId) {
        val book = repository.getOne(bookId)
        book.bookStatus = BookStatus.available
        book.memberId = null
        book.checkoutDate = null
        book.returnDate = null
        repository.save(book)
    }

    fun checkOutBook(
        bookId: BookId,
        memberId: MemberId,
        checkoutDate: LocalDate,
        returnDate: LocalDate
    ) {
        val book = repository.getOne(bookId)
        book.bookStatus = BookStatus.checkedOut
        book.memberId = memberId
        book.checkoutDate = checkoutDate
        book.returnDate = returnDate
        repository.save(book)
    }
}
