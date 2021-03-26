package digital.capsa.query.repo

import digital.capsa.core.aggregates.MemberId
import digital.capsa.query.model.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, MemberId>
