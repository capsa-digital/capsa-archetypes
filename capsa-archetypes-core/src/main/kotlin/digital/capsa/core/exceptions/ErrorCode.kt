package digital.capsa.core.exceptions

enum class ErrorCode(val code: Long) {
    APP_USER_VALIDATION_REQUIRED(10010L),
    BAD_CREDENTIALS(10020L),
    BAD_REQUEST(10030L)
}


