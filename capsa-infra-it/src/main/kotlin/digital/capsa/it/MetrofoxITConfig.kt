package digital.capsa.it

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class MetrofoxITConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}
