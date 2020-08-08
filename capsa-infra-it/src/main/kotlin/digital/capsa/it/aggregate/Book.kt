package digital.capsa.it.aggregate

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.core.vocab.AggregateType
import java.util.Random
import java.util.UUID

class Book(var volume: String? = null
) : AbstractAggregate("Book") {

    override fun construct() {
        val index = parent?.getChildCount(Book::class) ?: 0
        volume = volume ?: mockVolume(index)
    }

    override fun onCreate(context: AggregateBuilderContext) {
        addBook(context)
        logger.info("===> Book added, attr = ${getAttributes()}")
    }

    private fun addBook(context: AggregateBuilderContext) {
        val response = context.httpManager.sendHttpRequest("/requests/add-book.json",
                context.memento,
                mapOf(
                        "$.schema" to context.environment.getProperty("capsa.schema")!!,
                        "$.host" to context.environment.getProperty("capsa.command.host")!!,
                        "$.port" to context.environment.getProperty("capsa.command.port")!!,
                        "$.body.libraryId" to parent!!.id.toString(),
                        "$.body.volume" to volume
                )
        )
        val ids = ObjectMapper().readTree(response.body)?.get("ids")
        ids?.also {
            it.get(AggregateType.book.name)?.also { node ->
                id = UUID.fromString(node.asText())
            }
        }
    }

    companion object {
        val random = Random(0)

        private val volumes = listOf("jjl4BgAAQBAJ", "UeK1swEACAAJ", "xOO5yQEACAAJ", "ota_DwAAQBAJ", "Ev2wDAAAQBAJ")

        fun mockVolume(index: Int): String {
            return volumes[index % volumes.size]
        }
    }
}