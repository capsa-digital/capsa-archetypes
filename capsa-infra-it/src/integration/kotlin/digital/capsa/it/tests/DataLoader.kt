package digital.capsa.it.tests

import digital.capsa.it.aggregate.AggregateBuilderContext
import digital.capsa.it.aggregate.account
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class DataLoader : BeforeAllCallback {

    companion object {
        private var started = false
    }

    override fun beforeAll(extensionContext: ExtensionContext) {
        val profilesFromConsole = System.getProperty("spring.profiles.active", "")
        if (!profilesFromConsole.contains("prod") && !started) {
            started = true;
            buildDemoBusinessAccount().create(AggregateBuilderContext(applicationContext = SpringExtension.getApplicationContext(extensionContext)))
        }
    }

    private fun buildDemoBusinessAccount() =
            account {
                domain = "demo"
                library {
                    member { }
                    member { }
                    book { }
                    book { }
                }
                library {}
            }

}