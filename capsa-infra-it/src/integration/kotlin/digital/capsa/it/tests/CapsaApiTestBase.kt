package digital.capsa.it.tests

import digital.capsa.it.TestContext
import org.junit.Assume
import org.junit.jupiter.api.BeforeAll
import org.springframework.context.ApplicationContext

abstract class CapsaApiTestBase {

    companion object {

        lateinit var context: TestContext

        lateinit var schema: String

        lateinit var commandHost: String

        lateinit var commandPort: String

        lateinit var queryHost: String

        lateinit var queryPort: String

        @BeforeAll
        @JvmStatic
        fun beforeAll(applicationContext: ApplicationContext) {
            val profilesFromConsole = System.getProperty("spring.profiles.active", "")
            Assume.assumeFalse(profilesFromConsole.contains("prod"))

            context = TestContext(applicationContext = applicationContext)
            schema = context.environment.getProperty("capsa.schema")!!
            commandHost = context.environment.getProperty("capsa.command.host")!!
            commandPort = context.environment.getProperty("capsa.command.port")!!
            queryHost = context.environment.getProperty("capsa.query.host")!!
            queryPort = context.environment.getProperty("capsa.query.port")!!
        }
    }
}
