package digital.capsa.eventbus

import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel

interface AggregateOutput {

    @Output(OUTPUT)
    fun output(): MessageChannel

    companion object {
        const val OUTPUT = "aggregate-output"
    }
}
