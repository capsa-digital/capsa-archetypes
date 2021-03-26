package digital.capsa.archetypes.command.controller

import digital.capsa.archetypes.core.aggregates.AggregateId
import java.util.UUID

data class CommandResponse(
    val saga: UUID,
    val ids: List<AggregateId> = emptyList(),
    val payload: Any? = null
)
