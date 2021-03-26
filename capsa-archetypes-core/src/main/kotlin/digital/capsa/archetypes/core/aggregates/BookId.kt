package digital.capsa.archetypes.core.aggregates

import digital.capsa.core.aggregate.AggregateId
import java.util.UUID
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import javax.persistence.Embeddable

@Embeddable
data class BookId(
    val bookId: UUID
) : AggregateId

@Converter
class BookIdConverter : AttributeConverter<BookId?, UUID?> {

    override fun convertToDatabaseColumn(id: BookId?): UUID? {
        return id?.bookId
    }

    override fun convertToEntityAttribute(id: UUID?): BookId? {
        return id?.let { BookId(it) }
    }
}
