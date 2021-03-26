package digital.capsa.archetypes.eventbus.data

import digital.capsa.archetypes.core.aggregates.MemberId
import digital.capsa.archetypes.eventbus.EventData
import digital.capsa.archetypes.eventbus.PartitionKey

abstract class MemberEventData(
        @PartitionKey open var id: MemberId
) : EventData()
