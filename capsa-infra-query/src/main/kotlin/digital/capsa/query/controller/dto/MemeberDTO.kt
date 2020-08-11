package digital.capsa.query.controller.dto

import digital.capsa.core.vocab.MemberSortByType
import java.util.UUID

interface MemberDTO {
    val memberId: UUID
    val firstName: String
    val lastName: String
    val email: String
    val phone: String?
}

data class MemberInfoDTO(
        override var memberId: UUID,
        override var firstName: String,
        override var lastName: String,
        override var email: String,
        override var phone: String? = null
) : MemberDTO

data class MemberDetailsDTO(
        private val memberInfo: MemberInfoDTO
) : MemberDTO by memberInfo

data class MemberSearchCriteriaDTO(
        val pageNumber: Int = 1,
        val pageSize: Int = 15,
        val search: String?,
        val firstSortBy: MemberSortByType?,
        val firstSortDescending: Boolean?,
        val secondSortBy: MemberSortByType?,
        val secondSortDescending: Boolean?
)

data class MemberSearchResultDTO(
        val bookList: List<MemberInfoDTO>
)