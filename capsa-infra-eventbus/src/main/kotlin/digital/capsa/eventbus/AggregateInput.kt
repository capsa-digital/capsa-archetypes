package digital.capsa.eventbus

import org.springframework.cloud.stream.annotation.Input
import org.springframework.messaging.MessageChannel

interface AggregateInput {

    @Input(INPUT)
    fun input(): MessageChannel

    companion object {
        const val INPUT = "aggregate-input"
    }
}
