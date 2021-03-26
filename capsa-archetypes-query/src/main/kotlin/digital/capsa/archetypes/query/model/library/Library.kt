package digital.capsa.archetypes.query.model.library

import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.core.model.Address
import digital.capsa.core.aggregate.Aggregate
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "library")
@Table(name = "library")
data class Library(

    @Id
    override var id: LibraryId,

    var libraryName: String,

    @Embedded
    var address: Address
) : Aggregate<LibraryId>()
