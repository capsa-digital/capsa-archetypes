package digital.capsa.query.services

import digital.capsa.query.model.book.BookFilter
import digital.capsa.query.model.book.Book
import digital.capsa.query.model.book.BookSort
import digital.capsa.query.model.member.Member
import digital.capsa.query.repo.BookRepository
import digital.capsa.query.repo.MemberRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemberService(private val repository: MemberRepository) {

    fun getMember(memberId: UUID): Member {
        return repository.getOne(memberId)
    }

    // TODO
//    fun getBookList(agencyId: UUID, pageNumber: Int, pageSize: Int, search: String?, filter: BookFilter, sort: BookSort): List<Book> {
//        return repository.findAllLeadsByCriteria(agencyId, pageNumber, pageSize, search, filter, sort)
//    }

    fun createMember(member: Member) {
        if (repository.existsById(member.memberId)) {
            throw Error("Member with ${member.memberId} already exist")
        }
        repository.save(member)
    }
}
