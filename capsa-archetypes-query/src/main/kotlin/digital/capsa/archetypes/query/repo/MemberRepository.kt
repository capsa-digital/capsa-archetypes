package digital.capsa.archetypes.query.repo

import digital.capsa.archetypes.core.aggregates.MemberId
import digital.capsa.archetypes.query.model.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, MemberId>
