package digital.capsa.it

import digital.capsa.it.runner.HttpManager
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment

class TestContext(val applicationContext: ApplicationContext) {

    val memento: HashMap<String, String> = HashMap()

    val httpManager = applicationContext.getBean(HttpManager::class.java)

    val environment = applicationContext.getBean(Environment::class.java)

}