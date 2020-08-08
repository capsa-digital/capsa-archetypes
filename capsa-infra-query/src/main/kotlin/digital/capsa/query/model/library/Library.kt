package digital.capsa.query.model.library

import com.metrofoxsecurity.core.model.Address
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
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
