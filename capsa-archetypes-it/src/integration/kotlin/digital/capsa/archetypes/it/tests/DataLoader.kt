package digital.capsa.archetypes.it.tests

import digital.capsa.it.TestContext
import digital.capsa.archetypes.it.aggregate.account
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class DataLoader : BeforeAllCallback {

    companion object {
        private var started = false
    }

    override fun beforeAll(extensionContext: ExtensionContext) {
        if (!started) {
            started = true;
            buildInitialData().create(TestContext(applicationContext = SpringExtension.getApplicationContext(extensionContext)))
        }
    }

    // Use to load DB with data for load test
    private fun buildInitialData() =
            account {
                library { }
                member { }
            }
}