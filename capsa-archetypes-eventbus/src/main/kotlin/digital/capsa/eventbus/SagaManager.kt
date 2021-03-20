package digital.capsa.eventbus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SagaManager {

    @Autowired
    lateinit var eventPublisher: EventPublisher

    fun runSaga(
            vararg events: EventData?
    ): UUID {
        val sagaId = UUID.randomUUID()
        events.filterNotNull().forEach {
            it.sagaId = sagaId
            eventPublisher.publishEvent(it)
        }
        return sagaId
    }
}
