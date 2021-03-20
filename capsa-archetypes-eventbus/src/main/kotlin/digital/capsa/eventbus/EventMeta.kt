package digital.capsa.eventbus

import java.time.Instant

data class EventMeta(

        var correlationId: String?,

        var partitionKey: String?,

        var timestamp: Instant

)
