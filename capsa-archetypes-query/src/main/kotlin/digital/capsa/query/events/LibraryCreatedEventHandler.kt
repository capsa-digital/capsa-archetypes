package digital.capsa.query.events

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.eventbus.Event
import digital.capsa.eventbus.EventBusInput
import digital.capsa.eventbus.data.LibraryCreated
import digital.capsa.query.model.library.Library
import digital.capsa.query.services.LibraryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener

@EnableBinding(EventBusInput::class)
class LibraryCreatedEventHandler(private var libraryService: LibraryService) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @StreamListener(EventBusInput.INPUT, condition = "headers['eventType']=='LibraryCreated'")
    fun handleEvent(event: Event<LibraryCreated>) {
        logger.info("\n<==== ${objectMapper.writeValueAsString(event)}")
        libraryService.createLibrary(event.data.transform())
    }

    fun LibraryCreated.transform(): Library = Library(
            libraryId = libraryId,
            libraryName = libraryName,
            address = address
    )
}
