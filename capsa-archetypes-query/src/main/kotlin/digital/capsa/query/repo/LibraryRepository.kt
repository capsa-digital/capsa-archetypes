package digital.capsa.query.repo

import digital.capsa.core.aggregates.LibraryId
import digital.capsa.query.model.library.Library
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LibraryRepository : JpaRepository<Library, LibraryId>
