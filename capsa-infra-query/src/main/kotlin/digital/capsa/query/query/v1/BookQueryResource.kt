package digital.capsa.query.query.v1

import digital.capsa.query.model.book.Book
import digital.capsa.query.model.member.Member
import digital.capsa.query.query.v1.dto.BookInfoDTO
import digital.capsa.query.services.BookService
import digital.capsa.query.services.MemberService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function

@Service
class BookQueryResource {

    @Bean
    @Qualifier("getBookInfo")
    fun getBookInfo(bookService: BookService, memberService: MemberService): Function<UUID, BookInfoDTO?> {
        return Function { bookId ->
            val book = bookService.getBook(bookId)
            val member = book.memberId?.let { memberService.getMember(it) }
            return@Function book.bookInfo(member)
        }
    }


    fun Book.bookInfo(member: Member?): BookInfoDTO = BookInfoDTO(
            bookId = bookId,
            bookStatus = bookStatus,
            authorName = authorName,
            bookTitle = bookTitle,
            memberId = memberId,
            memberName = member?.name,
            memberEmail = member?.email
    )
}
