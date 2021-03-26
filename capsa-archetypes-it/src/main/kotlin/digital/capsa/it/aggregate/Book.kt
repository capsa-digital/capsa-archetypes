package digital.capsa.it.aggregate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.it.httpRequest

class Book(
    var volume: String? = null
) : AbstractAggregate("Book") {

    override fun construct() {
        val index = parent?.getChildCount(Book::class) ?: 0
        volume = volume ?: mockVolume(index)
    }

    override fun onCreate() {
        addBook()
        logger.info("===> Book added, attr = ${getAttributes()}")
    }

    private fun addBook() {
        httpRequest("/requests/add-book.json")
            .withTransformation(
                "$.schema" to context.environment.getProperty("capsa.schema"),
                "$.host" to context.environment.getProperty("capsa.host"),
                "$.port" to context.environment.getProperty("capsa.port"),
                "$.body.libraryId" to parent!!.id.toString(),
                "$.body.volume" to volume
            )
            .send {
                assertThat(statusCode.value()).isEqualTo(200)
                val ids = ObjectMapper().readTree(body)?.get("ids")
                ids?.also {
// TODO                        it.get(AggregateType.book.name)?.also { node ->
//                            id = UUID.fromString(node.asText())
//                        }
                }
            }
    }

    companion object {
        private val volumes = listOf(
            "jjl4BgAAQBAJ", "8UbNBQAAQBAJ", "xOO5yQEACAAJ", "K0qWBUOAf6IC", "Ev2wDAAAQBAJ",
            "eNF4zQEACAAJ", "DeTWDwAAQBAJ", "yHWADwAAQBAJ", "UQ3FCwAAQBAJ", "RpZGDwAAQBAJ",
            "czCiJ6sbhpAC", "UeK1swEACAAJ", "GjNjvJCIGIAC", "mpcBw1OnyIgC", "v5PHDwAAQBAJ",
            "xColAAPGubgC", "fGBwAAQBAJ", "8RueQPUOPssC", "2_gbZYZcZXgC", "ota_DwAAQBAJ"
        )

        fun mockVolume(index: Int): String {
            return volumes[index % volumes.size]
        }
    }
}