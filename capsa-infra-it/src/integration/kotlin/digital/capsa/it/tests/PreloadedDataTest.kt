package digital.capsa.it.tests

import digital.capsa.core.logger
import digital.capsa.it.runner.HttpManager
import org.junit.Assume
import org.junit.BeforeClass
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfSystemProperty
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals

@Tag("it")
@RunWith(SpringRunner::class)
@TestPropertySource(locations = ["classpath:application.properties"])
@EnableAutoConfiguration
@SpringBootTest(classes = [IntegrationConfig::class])
@ExtendWith(DataLoader::class)
class PreloadedDataTest {

    @Autowired
    lateinit var httpManager: HttpManager

    @BeforeClass
    fun disableTestsOnProd() {
        val profilesFromConsole = System.getProperty("spring.profiles.active", "")
        Assume.assumeFalse(profilesFromConsole.contains("prod"))
    }

    @Test
    @EnabledIfSystemProperty(named = "spring.profiles.active", matches = "local")
    fun verifyPreloadedLocalData(@Value("\${metrofox.host}") host: String,
                                 @Value("\${metrofox.schema}") schema: String,
                                 @Value("\${metrofox.port}") port: String) {
        val response = httpManager.sendHttpRequest(requestJsonFileName = "/requests/actuator-info.json",
                transformationData = mapOf(
                        "$.schema" to schema,
                        "$.host" to host,
                        "$.port" to port,
                        "$.path" to "/api/actuator/info"))
        assertEquals(200, response.statusCode.value())


        logger.info("++++++++++++verifyPreloadedLocalData")
    }

    @Test
    @EnabledIfSystemProperty(named = "spring.profiles.active", matches = "dev")
    fun verifyPreloadedDevData() {
        logger.info("++++++++++++verifyPreloadedDevData")
    }

    @Test
    @EnabledIfSystemProperty(named = "spring.profiles.active", matches = "qa")
    fun verifyPreloadedQaData() {
        logger.info("++++++++++++verifyPreloadedQaData")
    }
}