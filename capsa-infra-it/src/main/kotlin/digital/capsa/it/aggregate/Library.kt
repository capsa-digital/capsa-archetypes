package digital.capsa.it.aggregate

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.core.logger
import digital.capsa.core.model.Address
import digital.capsa.core.vocab.AggregateType
import digital.capsa.it.TestContext
import digital.capsa.it.runner.HttpManager
import org.springframework.core.env.Environment
import java.util.UUID

class Library(var libraryName: String? = null,
              var address: Address? = null
) : AbstractAggregate("Library") {

    fun book(init: Book.() -> Unit) = initAggregate(Book(), init)

    override fun construct() {
        libraryName = libraryName ?: mockLibraryName()
        address = address ?: mockAddress()
    }

    override fun onCreate(context: TestContext) {
        val applicationContext = context.applicationContext
        val httpManager = applicationContext.getBean(HttpManager::class.java)
        val environment = applicationContext.getBean(Environment::class.java)

        if (id == null) {
            createLibrary(httpManager, context)
        }
        logger.info("===> Library added, attr = ${getAttributes()}")
    }

    private fun createLibrary(httpManager: HttpManager, context: TestContext) {
        val response = httpManager.sendHttpRequest("/requests/create-library.json",
                context.memento,
                mapOf(
                        "$.schema" to context.environment.getProperty("capsa.schema"),
                        "$.host" to context.environment.getProperty("capsa.command.host"),
                        "$.port" to context.environment.getProperty("capsa.command.port"),
                        "$.body.libraryName" to libraryName,
                        "$.body.address" to address
                )
        )
        val ids = ObjectMapper().readTree(response.body)?.get("ids")
        ids?.also {
            it.get(AggregateType.library.name)?.also { node ->
                id = UUID.fromString(node.asText())
            }
        }
    }

    companion object {
        private val libraryNames = listOf("Royal Library", "National Library", "Central Library", "York Library", "Refference Library")

        private var libraryIndex = 0

        private var addressIndex = 0


        fun mockLibraryName(): String {
            return libraryNames[libraryIndex++ % libraryNames.size]
        }

        private val addresses = listOf(
                Pair("Toronto Branch", Address(
                        addressLine1 = "33 Victoria St",
                        city = "Toronto",
                        state = "ON",
                        zipCode = "M5C 2A1",
                        country = "Canada"
                )),
                Pair("Toronto Branch", Address(
                        addressLine1 = "441 Clark Ave W",
                        city = "Thornhill",
                        state = "ON",
                        zipCode = "L4J 6W8",
                        country = "Canada"
                )),
                Pair("Toronto Branch", Address(
                        addressLine1 = "7509 Yonge St",
                        city = "Thornhill",
                        state = "ON",
                        zipCode = "L3T 2B4",
                        country = "Canada"
                )),
                Pair("Montreal Branch", Address(
                        addressLine1 = "425 Boulevard de Maisonneuve O 3e étage",
                        city = "Montréal",
                        state = "QC",
                        zipCode = "H3A 3G5",
                        country = "Canada"

                )),
                Pair("Calgary Branch", Address(
                        addressLine1 = "700 2nd Street SW",
                        city = "Calgary",
                        state = "AB",
                        zipCode = "T2P 2W1",
                        country = "Canada"
                ))
        )

        fun mockAddress(): Address {
            return addresses[addressIndex++ % addresses.size].second
        }
    }
}

