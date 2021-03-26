package digital.capsa.archetypes.core.aggregates

import java.util.UUID
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import javax.persistence.Embeddable

@Embeddable
data class LibraryId(
    val libraryId: UUID
) : AggregateId

@Converter
class LibraryIdConverter : AttributeConverter<LibraryId?, UUID?> {

    override fun convertToDatabaseColumn(id: LibraryId?): UUID? {
        return id?.libraryId
    }

    override fun convertToEntityAttribute(id: UUID?): LibraryId? {
        return id?.let { LibraryId(it) }
    }
}
