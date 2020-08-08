package digital.capsa.it.tests

import digital.capsa.it.runner.HttpManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class IntegrationConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun httpManager(): HttpManager = HttpManager()
}
