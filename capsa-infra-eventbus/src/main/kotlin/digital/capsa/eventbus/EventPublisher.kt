package digital.capsa.eventbus

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.exceptions.EventPublishingException
import digital.capsa.core.logger
import digital.capsa.eventbus.repo.EventRecord
import digital.capsa.eventbus.repo.EventRepository
import org.apache.commons.text.StringEscapeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.core.env.Environment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

@EnableBinding(value = [EventBusOutput::class])
open class EventPublisher {

    @Autowired
    lateinit var eventTypeIdResolver: EventTypeIdResolver

    @Autowired
    private lateinit var eventBusOutput: EventBusOutput

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var environment: Environment

    @Suppress("TooGenericExceptionCaught")
    private fun publishEvent(event: Event<in EventData>) {
        val eventType = eventTypeIdResolver.idFromValue(event.data!!)
        val message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.MESSAGE_KEY, event.meta.partitionKey)
                .setHeader("eventType", eventType).build()
        try {
            eventBusOutput.output().send(message)
            val eventRecord = EventRecord(
                    id = event.id,
                    eventType = eventType,
                    correlationId = event.meta.correlationId,
                    partitionKey = event.meta.partitionKey,
                    timestamp = event.meta.timestamp,
                    payload = objectMapper.writeValueAsString(event.data))
            eventRepository.save(eventRecord)

            logger.info("\n====> ${StringEscapeUtils.unescapeJava(objectMapper.writeValueAsString(objectMapper.writeValueAsString(event.data)))}")
        } catch (t: Throwable) {
            throw EventPublishingException("Failed to publish event $event", t)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun publishEvent(eventData: EventData) {
        val partitionKeyFields = eventData::class.members.filter { property -> property.findAnnotation<PartitionKey>() != null }
        if (partitionKeyFields.size > 1) {
            throw Error("More than one PartitionKey found")
        }
        val partitionKeyValue = partitionKeyFields.getOrNull(0)?.let { (it as KProperty1<Any, *>).get(eventData).toString() }
        val event = Event(id = UUID.randomUUID(),
                meta = EventMeta(correlationId = UUID.randomUUID().toString(),
                        partitionKey = partitionKeyValue ?: "global",
                        timestamp = Instant.now()),
                data = eventData)
        publishEvent(event)
    }
}
