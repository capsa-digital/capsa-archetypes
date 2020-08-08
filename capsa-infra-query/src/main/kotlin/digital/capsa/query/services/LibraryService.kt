package digital.capsa.query.services

import digital.capsa.query.model.library.Library
import digital.capsa.query.repo.LibraryRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LibraryService(private val repository: LibraryRepository) {

    fun getLibrary(libraryId: UUID): Library {
        return repository.getOne(libraryId)
    }

    fun getLibraryList(): List<Library> {
        return repository.findAll()
    }

    fun createLibrary(library: Library) {
        if (repository.existsById(library.libraryId)) {
            throw Error("Library with ${library.libraryId} already exist")
        }
        repository.save(library)
    }
}
