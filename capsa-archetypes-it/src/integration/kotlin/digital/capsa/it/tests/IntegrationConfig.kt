package digital.capsa.it.tests

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class IntegrationConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}
