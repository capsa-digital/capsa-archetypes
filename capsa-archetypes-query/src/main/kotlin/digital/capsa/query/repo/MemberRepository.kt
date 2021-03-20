package digital.capsa.query.repo

import digital.capsa.query.model.book.Book
import digital.capsa.query.model.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<Member, UUID>
