package digital.capsa.archetypes.it

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import digital.capsa.archetypes.eventbus.EventBusInput
import digital.capsa.archetypes.it.event.EventSnooper
import digital.capsa.core.logger
import digital.capsa.it.TestContext
import digital.capsa.it.runner.HttpRequestBuilder
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.net.URL
import java.util.concurrent.TimeUnit

@Tag("it")
@ExtendWith(SpringExtension::class)
@TestPropertySource(locations = ["classpath:application.yml"])
@SpringBootTest(classes = [TestConfig::class, RestTemplate::class, EventSnooper::class, EventBusInput::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(
    exclude = [
        DataSourceAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class
    ]
)
abstract class TestBase {

    protected var driver: ThreadLocal<WebDriver> = ThreadLocal<WebDriver>()

    fun getDriver(): WebDriver? {
        return driver.get()
    }

    @BeforeAll
    fun setUp() {
        try {
            logger.info("Trying to start WireMock")
            if (!wireMockHost.isNullOrBlank()) {
                WireMock.configureFor(
                    WireMock.create().scheme(wireMockSchema).host(wireMockHost).port(wireMockPort!!.toInt()).urlPathPrefix(
                        wireMockBasePath
                    ).proxyHost(wireMockProxyHost).proxyPort(wireMockProxyPort?.toInt() ?: 0).build()
                )
            }
        } catch (e: Exception) {
            logger.error("WireMock failed", e)
        }

        var webDriver: RemoteWebDriver? = null
        try {
            logger.info("Trying to start WebDriver")
            if (!webDriverHost.isNullOrBlank()) {
                webDriver = RemoteWebDriver(
                    URL("${webDriverSchema}://${webDriverHost}:${webDriverPort}/wd/hub"),
                    ChromeOptions()
                        .apply { addArguments("start-maximized") } // open Browser in maximized mode
                        .apply { addArguments("disable-infobars") } // disabling infobars
                        .apply { addArguments("--disable-extensions") } // disabling extensions
                        .apply { addArguments("--disable-gpu") } // applicable to windows os only
                        .apply { addArguments("--disable-dev-shm-usage") } // overcome limited resource problems
                        .apply { addArguments("--no-sandbox") }  // Bypass OS security model
                )
            }
        } catch (e: Exception) {
            logger.error("WebDriver failed", e)
        }

        webDriver?.let {
            it.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS)
            driver.set(webDriver)
        }
    }


    companion object {

        lateinit var objectMapper: ObjectMapper

        lateinit var context: TestContext

        lateinit var uiSchema: String

        lateinit var uiHost: String

        lateinit var uiPort: String

        lateinit var uiBasePath: String

        var uiProxyHost: String? = null

        var uiProxyPort: String? = null

        lateinit var apiSchema: String

        lateinit var apiHost: String

        lateinit var apiPort: String

        lateinit var apiBasePath: String

        var apiProxyHost: String? = null

        var apiProxyPort: String? = null

        var wireMockSchema: String? = null

        var wireMockHost: String? = null

        var wireMockPort: String? = null

        var wireMockBasePath: String? = null

        var wireMockProxyHost: String? = null

        var wireMockProxyPort: String? = null

        var webDriverSchema: String? = null

        var webDriverHost: String? = null

        var webDriverPort: String? = null

        var webDriverProxyHost: String? = null

        var webDriverProxyPort: String? = null

        @BeforeAll
        @JvmStatic
        fun beforeAll(applicationContext: ApplicationContext) {
            Assumptions.assumeFalse(System.getProperty("spring.profiles.active", "").contains("prod"))

            objectMapper = applicationContext.getBean(ObjectMapper::class.java)
            context = TestContext(applicationContext = applicationContext)
            uiSchema = context.environment.getProperty("ui.schema")!!
            uiHost = context.environment.getProperty("ui.host")!!
            uiPort = context.environment.getProperty("ui.port")!!
            uiBasePath = context.environment.getProperty("ui.basePath")!!
            uiProxyHost = context.environment.getProperty("ui.proxyHost")
            uiProxyPort = context.environment.getProperty("ui.proxyPort")
            apiSchema = context.environment.getProperty("api.schema")!!
            apiHost = context.environment.getProperty("api.host")!!
            apiPort = context.environment.getProperty("api.port")!!
            apiBasePath = context.environment.getProperty("api.basePath")!!
            apiProxyHost = context.environment.getProperty("api.proxyHost")
            apiProxyPort = context.environment.getProperty("api.proxyPort")
            wireMockSchema = context.environment.getProperty("wireMock.schema")
            wireMockHost = context.environment.getProperty("wireMock.host")
            wireMockPort = context.environment.getProperty("wireMock.port")
            wireMockBasePath = context.environment.getProperty("wireMock.basePath")
            wireMockProxyHost = context.environment.getProperty("wireMock.proxyHost")
            wireMockProxyPort = context.environment.getProperty("wireMock.proxyPort")
            webDriverSchema = context.environment.getProperty("webDriver.schema")
            webDriverHost = context.environment.getProperty("webDriver.host")
            webDriverPort = context.environment.getProperty("webDriver.port")
            webDriverProxyHost = context.environment.getProperty("webDriver.proxyHost")
            webDriverProxyPort = context.environment.getProperty("webDriver.proxyPort")

        }
    }
}

fun httpRequest(requestJsonFileName: String): HttpRequestBuilder {
    return HttpRequestBuilder(TestBase.objectMapper, requestJsonFileName)
}