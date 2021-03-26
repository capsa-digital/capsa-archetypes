package digital.capsa.archetypes.core.exceptions

open class CatastrophicFailureException(message: String, t: Throwable) : RuntimeException(message, t)

class EventPublishingException(message: String, t: Throwable) : CatastrophicFailureException(message, t)
