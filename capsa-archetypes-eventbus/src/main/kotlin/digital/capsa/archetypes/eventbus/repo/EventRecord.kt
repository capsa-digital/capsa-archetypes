package digital.capsa.archetypes.eventbus.repo

import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "event_store")
class EventRecord(
        @Id
        val id: UUID,
        val eventType: String,
        val correlationId: String?,
        val partitionKey: String?,
        val timestamp: Instant,
        @Column(columnDefinition = "TEXT")
        val payload: String
)
