package digital.capsa.it.tests

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.it.TestContext
import digital.capsa.it.runner.HttpRequestBuilder
import org.junit.Assume
import org.junit.jupiter.api.BeforeAll
import org.springframework.context.ApplicationContext

abstract class CapsaApiTestBase {

    companion object {

        lateinit var objectMapper: ObjectMapper

        lateinit var context: TestContext

        lateinit var schema: String

        lateinit var appHost: String

        lateinit var appPort: String

        @BeforeAll
        @JvmStatic
        fun beforeAll(applicationContext: ApplicationContext) {
            val profilesFromConsole = System.getProperty("spring.profiles.active", "")
            Assume.assumeFalse(profilesFromConsole.contains("prod"))

            objectMapper = applicationContext.getBean(ObjectMapper::class.java)
            context = TestContext(applicationContext = applicationContext)
            schema = context.environment.getProperty("capsa.schema")!!
            appHost = context.environment.getProperty("capsa.host")!!
            appPort = context.environment.getProperty("capsa.port")!!
        }
    }
}

fun httpRequest(requestJsonFileName: String): HttpRequestBuilder {
    return HttpRequestBuilder(CapsaApiTestBase.objectMapper, requestJsonFileName)
}