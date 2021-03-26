package digital.capsa.eventbus.data

import digital.capsa.core.aggregates.LibraryId
import digital.capsa.eventbus.EventData
import digital.capsa.eventbus.PartitionKey

abstract class LibraryEventData(
    @PartitionKey open var id: LibraryId
) : EventData()
