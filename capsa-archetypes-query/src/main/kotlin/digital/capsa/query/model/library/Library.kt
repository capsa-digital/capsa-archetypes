package digital.capsa.query.model.library

import digital.capsa.core.model.Address
import java.util.UUID
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "library")
@Table(name = "library")
data class Library(

        @Id
        var libraryId: UUID,

        var libraryName: String,

        @Embedded
        var address: Address
)
