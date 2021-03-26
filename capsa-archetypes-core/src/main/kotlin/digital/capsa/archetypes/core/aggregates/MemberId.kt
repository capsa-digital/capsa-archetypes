package digital.capsa.archetypes.core.aggregates

import digital.capsa.core.aggregate.AggregateId
import java.util.UUID
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import javax.persistence.Embeddable

@Embeddable
data class MemberId(
    val memberId: UUID
) : AggregateId

@Converter
class MemberIdConverter : AttributeConverter<MemberId?, UUID?> {

    override fun convertToDatabaseColumn(id: MemberId?): UUID? {
        return id?.memberId
    }

    override fun convertToEntityAttribute(id: UUID?): MemberId? {
        return id?.let { MemberId(it) }
    }
}
