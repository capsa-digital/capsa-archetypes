package digital.capsa.eventbus.data

import digital.capsa.core.aggregates.BookId
import digital.capsa.eventbus.EventData
import digital.capsa.eventbus.PartitionKey

abstract class BookEventData(
    @PartitionKey open var id: BookId
) : EventData()
