package digital.capsa.query.controller

import digital.capsa.core.aggregates.BookId
import digital.capsa.core.aggregates.LibraryId
import digital.capsa.core.aggregates.MemberId
import digital.capsa.query.controller.dto.BookDetailsDTO
import digital.capsa.query.controller.dto.BookInfoDTO
import digital.capsa.query.controller.dto.BookSearchCriteriaDTO
import digital.capsa.query.controller.dto.BookSearchResultDTO
import digital.capsa.query.controller.dto.MemberDetailsDTO
import digital.capsa.query.controller.dto.MemberInfoDTO
import digital.capsa.query.controller.dto.MemberSearchCriteriaDTO
import digital.capsa.query.model.book.Book
import digital.capsa.query.model.member.Member
import digital.capsa.query.services.BookService
import digital.capsa.query.services.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.function.Function

@Service
class QueryController {

    @Bean
    fun getBookList(bookService: BookService): Function<BookSearchCriteriaDTO, BookSearchResultDTO> {
        return Function { criteria ->
            return@Function bookService.getBookList(LibraryId(criteria.libraryId)).transform()
        }
    }

    @Bean
    fun getBookDetails(bookService: BookService, memberService: MemberService): Function<UUID, BookDetailsDTO?> {
        return Function { bookId ->
            val book = bookService.getBook(BookId(bookId))
            val member = book.memberId?.let { memberService.getMember(it) }
            return@Function book.bookDetails(member)
        }
    }

    fun Book.bookInfo(): BookInfoDTO = BookInfoDTO(
        bookId = id.bookId,
        bookStatus = bookStatus,
        authorName = authorName,
        bookTitle = bookTitle,
        memberId = memberId?.memberId
    )

    fun Book.bookDetails(member: Member?): BookDetailsDTO = BookDetailsDTO(
        bookInfo = this.bookInfo(),
        memberFirstName = member?.firstName,
        memberLastName = member?.lastName,
        memberEmail = member?.email
    )

    fun List<Book>.transform(): BookSearchResultDTO = BookSearchResultDTO(bookList = map { book -> book.bookInfo() })

    @Bean
    fun getMemberList(memberService: MemberService): Function<MemberSearchCriteriaDTO, List<MemberInfoDTO>> {
        return Function {
            return@Function memberService.getMemberList().map { it.memberInfo() }
        }
    }

    @Bean
    fun getMemberDetails(memberService: MemberService): Function<UUID, MemberDetailsDTO?> {
        return Function { memberId ->
            val member = memberService.getMember(MemberId(memberId))
            return@Function member.memberDetails(member)
        }
    }

    fun Member.memberDetails(member: Member?): MemberDetailsDTO = MemberDetailsDTO(
        memberInfo = this.memberInfo()
    )

    fun Member.memberInfo(): MemberInfoDTO = MemberInfoDTO(
        memberId = id.memberId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone
    )

}
