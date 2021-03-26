package digital.capsa.archetypes.command.controller

import digital.capsa.core.aggregate.AggregateId
import java.util.UUID

data class CommandResponse(
    val saga: UUID,
    val ids: List<AggregateId> = emptyList(),
    val payload: Any? = null
)
