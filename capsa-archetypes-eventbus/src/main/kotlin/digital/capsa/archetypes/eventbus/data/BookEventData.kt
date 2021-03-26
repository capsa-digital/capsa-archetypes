package digital.capsa.archetypes.eventbus.data

import digital.capsa.archetypes.core.aggregates.BookId
import digital.capsa.archetypes.eventbus.EventData
import digital.capsa.archetypes.eventbus.PartitionKey

abstract class BookEventData(
    @PartitionKey open var id: BookId
) : EventData()
