package digital.capsa.archetypes.query.services

import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.query.model.library.Library
import digital.capsa.archetypes.query.repo.LibraryRepository
import org.springframework.stereotype.Component

@Component
class LibraryService(private val repository: LibraryRepository) {

    fun getLibrary(libraryId: LibraryId): Library {
        return repository.getOne(libraryId)
    }

    fun getLibraryList(): List<Library> {
        return repository.findAll()
    }

    fun createLibrary(library: Library) {
        if (repository.existsById(library.id)) {
            throw Error("Library with ${library.id} already exist")
        }
        repository.save(library)
    }
}
