package digital.capsa.archetypes.eventbus.data

import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.eventbus.EventData
import digital.capsa.archetypes.eventbus.PartitionKey

abstract class LibraryEventData(
    @PartitionKey open var id: LibraryId
) : EventData()
