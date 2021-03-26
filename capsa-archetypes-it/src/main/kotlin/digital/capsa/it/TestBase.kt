package digital.capsa.it

import com.fasterxml.jackson.databind.ObjectMapper
import digital.capsa.it.runner.HttpRequestBuilder
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@Tag("it")
@ExtendWith(SpringExtension::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@SpringBootTest(classes = [TestConfig::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class
    ]
)
abstract class TestBase {

    companion object {

        lateinit var objectMapper: ObjectMapper

        lateinit var context: TestContext

        lateinit var appSchema: String

        lateinit var appHost: String

        lateinit var appPort: String

        @BeforeAll
        @JvmStatic
        fun beforeAll(applicationContext: ApplicationContext) {
            Assumptions.assumeFalse(System.getProperty("spring.profiles.active", "").contains("prod"))

            objectMapper = applicationContext.getBean(ObjectMapper::class.java)
            context = TestContext(applicationContext = applicationContext)
            appSchema = context.environment.getProperty("capsa.schema")!!
            appHost = context.environment.getProperty("capsa.host")!!
            appPort = context.environment.getProperty("capsa.port")!!
        }
    }
}

fun httpRequest(requestJsonFileName: String): HttpRequestBuilder {
    return HttpRequestBuilder(TestBase.objectMapper, requestJsonFileName)
}