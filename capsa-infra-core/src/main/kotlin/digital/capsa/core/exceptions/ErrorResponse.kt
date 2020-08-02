package digital.capsa.core.exceptions

import org.springframework.http.HttpStatus
import java.util.Date

data class ErrorResponse(
        val timestamp: Date,
        val httpStatus: HttpStatus,
        val message: String?,
        val errorCode: Long?,
        val cause: String?
) {
    companion object {
        tailrec fun getCause(t: Throwable): Throwable {
            return if (t.cause != null) getCause(t.cause as Throwable) else t
        }
    }
}
