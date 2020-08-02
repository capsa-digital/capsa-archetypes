package digital.capsa.command.controller

import digital.capsa.core.vocab.AggregateType
import java.util.UUID

data class CommandResponse(
        val saga: UUID,
        val ids: Map<AggregateType, UUID> = emptyMap(),
        val payload: Any? = null
)
