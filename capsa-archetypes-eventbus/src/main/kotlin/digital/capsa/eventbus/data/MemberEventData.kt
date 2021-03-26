package digital.capsa.eventbus.data

import digital.capsa.core.aggregates.MemberId
import digital.capsa.eventbus.EventData
import digital.capsa.eventbus.PartitionKey
import java.util.UUID

abstract class MemberEventData(
        @PartitionKey open var id: MemberId
) : EventData()
