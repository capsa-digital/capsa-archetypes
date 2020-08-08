package digital.capsa.query.repo

import digital.capsa.query.model.library.Library
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LibraryRepository : JpaRepository<Library, UUID>
