package digital.capsa.archetypes.it.aggregate

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import digital.capsa.core.logger
import digital.capsa.archetypes.core.model.Address
import digital.capsa.archetypes.it.httpRequest
import digital.capsa.it.aggregate.AbstractAggregate
import java.util.UUID

class Library(
    var libraryName: String? = null,
    var address: Address? = null
) : AbstractAggregate("Library") {

    fun book(init: Book.() -> Unit) = initAggregate(Book(), init)

    override fun construct() {
        val library = mockLibrary()
        libraryName = libraryName ?: library.first
        address = address ?: library.second
    }

    override fun onCreate() {
        if (id == null) {
            createLibrary()
        }
        logger.info("===> Library added, attr = ${getAttributes()}")
    }

    private fun createLibrary() {
        httpRequest("/requests/create-library.json")
            .withTransformation(
                "$.schema" to context.environment.getProperty("api.schema"),
                "$.host" to context.environment.getProperty("api.host"),
                "$.port" to context.environment.getProperty("api.port"),
                "$.body.libraryName" to libraryName,
                "$.body.address" to address
            )
            .send {
                assertThat(statusCode.value()).isEqualTo(200)
                val ids = ObjectMapper().readTree(body)?.get("ids")
                ids?.also { idsNode ->
                    (idsNode as ArrayNode).forEach { idNode ->
                        when (idNode.fields().next().key) {
                            "libraryId" -> id = UUID.fromString(idNode["libraryId"].asText())
                        }
                    }
                }
            }
    }

    companion object {
        private var libraryIndex = 0

        fun mockLibrary(): Pair<String, Address> {
            return libraries[libraryIndex++ % libraries.size]
        }

        private val libraries = listOf(
            Pair(
                "Camp Chicka Chicka Boom Boom", Address(
                    addressLine1 = "33 Victoria St",
                    city = "Toronto",
                    state = "ON",
                    zipCode = "M5C 2A1",
                    country = "Canada"
                )
            ),
            Pair(
                "Enchanting Library", Address(
                    addressLine1 = "441 Clark Ave W",
                    city = "Thornhill",
                    state = "ON",
                    zipCode = "L4J 6W8",
                    country = "Canada"
                )
            ),
            Pair(
                "Libby Friends of the Library", Address(
                    addressLine1 = "7509 Yonge St",
                    city = "Thornhill",
                    state = "ON",
                    zipCode = "L3T 2B4",
                    country = "Canada"
                )
            ),
            Pair(
                "Little Book House", Address(
                    addressLine1 = "425 Boulevard de Maisonneuve O 3e étage",
                    city = "Montréal",
                    state = "QC",
                    zipCode = "H3A 3G5",
                    country = "Canada"

                )
            ),
            Pair(
                "Peace Center Books", Address(
                    addressLine1 = "700 2nd Street SW",
                    city = "Calgary",
                    state = "AB",
                    zipCode = "T2P 2W1",
                    country = "Canada"
                )
            ),
            Pair(
                "Little Free Food Pantry", Address(
                    addressLine1 = "208 West Clark St",
                    city = "Colby",
                    state = "WI",
                    zipCode = "54421",
                    country = "US"
                )
            ),
            Pair(
                "Gerlinde", Address(
                    addressLine1 = "1013 LITHIA WAY",
                    city = "TALENT",
                    state = "OR",
                    zipCode = "97540",
                    country = "US"
                )
            ),
            Pair(
                "Little Book House", Address(
                    addressLine1 = "12216 Dovercourt Crescent NW",
                    city = "Edmonton",
                    state = "AB",
                    zipCode = "T5L 4E5",
                    country = "Canada"
                )
            )
        )
    }
}

