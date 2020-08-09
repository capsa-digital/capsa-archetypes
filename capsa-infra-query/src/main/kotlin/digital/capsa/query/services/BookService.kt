package digital.capsa.query.services

import digital.capsa.core.vocab.BookStatus
import digital.capsa.query.model.book.Book
import digital.capsa.query.repo.BookRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.UUID

@Component
class BookService(private val repository: BookRepository) {

    fun getBook(bookId: UUID): Book {
        return repository.getOne(bookId)
    }

    //TODO
//    fun getBookList(agencyId: UUID, pageNumber: Int, pageSize: Int, search: String?, filter: BookFilter, sort: BookSort): List<Book> {
//        return repository.findAllLeadsByCriteria(agencyId, pageNumber, pageSize, search, filter, sort)
//    }

    fun addBook(book: Book) {
        if (repository.existsById(book.bookId)) {
            throw Error("Book with ${book.bookId} already exist")
        }
        repository.save(book)
    }

    fun checkInBook(bookId: UUID) {
        val book = repository.getOne(bookId)
        book.bookStatus = BookStatus.available
        book.memberId = null
        book.checkoutDate = null
        book.returnDate = null
        repository.save(book)
    }

    fun checkOutBook(bookId: UUID,
                     memberId: UUID,
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
