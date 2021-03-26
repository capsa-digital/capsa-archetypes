package digital.capsa.archetypes.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.archetypes.eventbus.Event
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.eventbus.data.LibraryCreated
import digital.capsa.archetypes.query.model.library.Library
import digital.capsa.archetypes.query.services.LibraryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
open class LibraryCreatedEventHandler(private var libraryService: LibraryService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='LibraryCreated'")
    fun handleEvent(event: Event<LibraryCreated>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        libraryService.createLibrary(event.data.transform())
    }

    fun LibraryCreated.transform(): Library = Library(
        id = id,
        libraryName = libraryName,
        address = address
    )
}
