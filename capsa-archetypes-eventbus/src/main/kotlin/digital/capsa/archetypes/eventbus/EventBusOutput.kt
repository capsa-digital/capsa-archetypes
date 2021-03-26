package digital.capsa.archetypes.eventbus

import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel

interface EventBusOutput {

    @Output(OUTPUT)
    fun output(): MessageChannel

    companion object {
        const val OUTPUT = "event-bus-output"
    }
}
