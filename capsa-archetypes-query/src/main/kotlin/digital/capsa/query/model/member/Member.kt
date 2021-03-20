package digital.capsa.query.model.member

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "member")
@Table(name = "member")
data class Member(

        @Id
        var memberId: UUID,

        var firstName: String,

        var lastName: String,

        var email: String,

        var phone: String? = null
)
