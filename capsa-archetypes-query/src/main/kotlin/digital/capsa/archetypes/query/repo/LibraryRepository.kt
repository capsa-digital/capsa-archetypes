package digital.capsa.archetypes.query.repo

import digital.capsa.archetypes.core.aggregates.LibraryId
import digital.capsa.archetypes.query.model.library.Library
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LibraryRepository : JpaRepository<Library, LibraryId>
