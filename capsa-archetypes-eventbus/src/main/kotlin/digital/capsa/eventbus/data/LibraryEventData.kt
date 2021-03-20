package digital.capsa.eventbus.data

import digital.capsa.eventbus.EventData
import digital.capsa.eventbus.PartitionKey
import java.util.UUID

abstract class LibraryEventData(
        @PartitionKey open var libraryId: UUID
) : EventData()
