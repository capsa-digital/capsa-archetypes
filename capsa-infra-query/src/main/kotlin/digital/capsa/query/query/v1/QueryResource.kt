package digital.capsa.query.query.v1

import digital.capsa.query.model.book.Book
import digital.capsa.query.model.member.Member
import digital.capsa.query.query.v1.dto.BookDetailsDTO
import digital.capsa.query.query.v1.dto.BookInfoDTO
import digital.capsa.query.query.v1.dto.MemberInfoDTO
import digital.capsa.query.services.BookService
import digital.capsa.query.services.MemberService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function
import java.util.function.Supplier

@Service
class QueryResource {

    @Bean
    @Qualifier("getAllBooks")
    fun getAllBooks(bookService: BookService): Supplier<List<BookInfoDTO>> {
        return Supplier {
            return@Supplier bookService.getAllBooks().map { it.bookInfo() }
        }
    }

    @Bean
    @Qualifier("getAllMembers")
    fun getAllMembers(memberService: MemberService): Supplier<List<MemberInfoDTO>> {
        return Supplier {
            return@Supplier memberService.getAllMembers().map { it.memberInfo() }
        }
    }

    @Bean
    @Qualifier("getBookDetails")
    fun getBookDetails(bookService: BookService, memberService: MemberService): Function<UUID, BookDetailsDTO?> {
        return Function { bookId ->
            val book = bookService.getBook(bookId)
            val member = book.memberId?.let { memberService.getMember(it) }
            return@Function book.bookDetails(member)
        }
    }

    fun Book.bookInfo(): BookInfoDTO = BookInfoDTO(
            bookId = bookId,
            bookStatus = bookStatus,
            authorName = authorName,
            bookTitle = bookTitle,
            memberId = memberId
    )

    fun Book.bookDetails(member: Member?): BookDetailsDTO = BookDetailsDTO(
            bookInfo = this.bookInfo(),
            memberFirstName = member?.firstName,
            memberLastName = member?.lastName,
            memberEmail = member?.email
    )

    fun Member.memberInfo(): MemberInfoDTO = MemberInfoDTO(
            memberId = memberId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone
    )
}
