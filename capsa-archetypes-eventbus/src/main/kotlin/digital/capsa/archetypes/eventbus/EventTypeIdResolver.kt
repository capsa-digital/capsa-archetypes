package digital.capsa.archetypes.eventbus

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Service
import java.lang.Class.forName

@Service
class EventTypeIdResolver : TypeIdResolverBase() {

    override fun idFromValue(value: Any): String {
        return idFromValueAndType(value, value::class.java)
    }

    override fun idFromValueAndType(value: Any, suggestedType: Class<*>): String {
        return getJsonTypeNameByClass(value::class.java)
    }

    override fun getMechanism(): JsonTypeInfo.Id {
        return JsonTypeInfo.Id.CUSTOM
    }

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        return getClassByJsonValue(id).let(context::constructType)
    }

    private val jsonTypeNameAnnotatedClasses by lazy {
        val scanner = object : ClassPathScanningCandidateComponentProvider(false) {}
        scanner.addIncludeFilter(AnnotationTypeFilter(JsonTypeName::class.java))
        return@lazy listOf(
                EventData::class.java.`package`.name,
                EventMeta::class.java.`package`.name
        ).flatMap { packageName ->
            scanner.findCandidateComponents(packageName).map(BeanDefinition::getBeanClassName).map(::forName)
        }
    }

    private fun getClassByJsonValue(value: String): Class<*> {
        return jsonTypeNameAnnotatedClasses
                .first { c -> c.getAnnotation(JsonTypeName::class.java).value == value }
    }

    private fun getJsonTypeNameByClass(clazz: Class<*>): String {
        try {
            return jsonTypeNameAnnotatedClasses
                    .first { c -> c === clazz }
                    .let { c -> c.getAnnotation(JsonTypeName::class.java).value }
        } catch (e: Exception) {
            throw Error("JsonTypeName annotation not found: " + clazz)
        }
    }
}
