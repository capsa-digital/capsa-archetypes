package digital.capsa.query

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
class QueryConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/")
                .setViewName("index")
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE)
    }

    @Bean
    fun templateResolver(): ITemplateResolver? {
        val resolver = ClassLoaderTemplateResolver()
        resolver.prefix = "templates/library/"
        resolver.suffix = ".html"
        resolver.templateMode = TemplateMode.HTML
        resolver.characterEncoding = "UTF-8"
        return resolver
    }
}
