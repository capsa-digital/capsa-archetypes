package digital.capsa.it.event

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.eventbus.Event
import digital.capsa.eventbus.EventBusInput
import digital.capsa.eventbus.EventData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import java.util.Collections

@EnableBinding(EventBusInput::class)
class EventSnooper() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val eventBuffer = mutableListOf<Event<EventData>>()

    @StreamListener(EventBusInput.INPUT)
    fun handleEvent(event: Event<EventData>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        eventBuffer.add(event)
    }

    fun getEvents(): List<Event<EventData>> {
        return Collections.unmodifiableList(eventBuffer)
    }

    fun clear() {
        eventBuffer.clear()
    }
}
