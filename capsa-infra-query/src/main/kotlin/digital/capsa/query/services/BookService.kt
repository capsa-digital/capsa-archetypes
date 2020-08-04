package digital.capsa.query.services

import digital.capsa.query.model.book.BookFilter
import digital.capsa.query.model.book.Book
import digital.capsa.query.model.book.BookSort
import digital.capsa.query.repo.BookRepository
import org.springframework.stereotype.Component
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

    fun createBook(book: Book) {
        if (repository.existsById(book.bookId)) {
            throw Error("Book with ${book.bookId} already exist")
        }
        repository.save(book)
    }
}
