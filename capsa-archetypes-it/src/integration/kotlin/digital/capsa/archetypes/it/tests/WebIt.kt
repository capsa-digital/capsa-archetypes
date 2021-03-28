package digital.capsa.archetypes.it.tests

import digital.capsa.archetypes.it.TestBase
import digital.capsa.it.dsl.given
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfSystemProperty
import kotlin.test.assertEquals

@DisplayName("Web Tests")
@DisabledIfSystemProperty(named = "spring.profiles.active", matches = "^qa$")
class WebIt : TestBase() {

    @Test
    fun `web title test`() {
        given {
        }.on {
            getDriver()!!.also { driver ->
                driver.navigate().to("$uiSchema://$uiHost:$uiPort$uiBasePath")
            }
        }.then { driver ->
            assertEquals("Capsa UI", driver.title)
        }
    }
}