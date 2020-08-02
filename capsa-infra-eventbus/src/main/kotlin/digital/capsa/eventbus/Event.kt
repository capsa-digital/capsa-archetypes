package digital.capsa.eventbus

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
data class Event<TEventData : EventData>(

        var id: UUID,

        var meta: EventMeta,

        var data: TEventData

)
