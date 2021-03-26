package digital.capsa.archetypes.eventbus

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "eventType")
@JsonTypeIdResolver(EventTypeIdResolver::class)
abstract class EventData(
        var sagaId: UUID? = null
)
