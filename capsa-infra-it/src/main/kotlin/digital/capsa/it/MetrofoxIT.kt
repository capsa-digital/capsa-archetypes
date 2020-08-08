package digital.capsa.it

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest

@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty",
    "html:build/reports/cucumber/cucumber-html-report",
    "json:build/reports/cucumber/cucumber-json-report.json"],
        features = ["classpath:features"],
        glue = ["com.metrofoxsecurity"])
@SpringBootTest
class MetrofoxIT
