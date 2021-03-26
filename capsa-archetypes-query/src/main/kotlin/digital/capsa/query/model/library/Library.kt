package digital.capsa.query.model.library

import digital.capsa.core.aggregates.Aggregate
import digital.capsa.core.aggregates.LibraryId
import digital.capsa.core.model.Address
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
