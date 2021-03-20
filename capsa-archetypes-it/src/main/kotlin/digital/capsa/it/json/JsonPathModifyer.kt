package digital.capsa.it.json

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom
import java.util.regex.Pattern
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.streams.asSequence

object JsonPathModifyer {

    init {
        setIdeaIoUseFallback()
    }

    fun modifyJson(json: String, transformationData: Map<String, Any?>, properties: Map<String, String?>? = null): String {
        var result: Any?
        val document = JsonPath.using(Configuration.defaultConfiguration()).parse(json)

        for ((path, value) in transformationData) {
            val propertyMatcher = Pattern.compile("(.*)\\[\\[(.*)]](.*)").matcher(value.toString())
            val _value = if (properties != null && propertyMatcher.matches()) {
                propertyMatcher.group(1) +
                        properties[propertyMatcher.group(2)].toString() +
                        propertyMatcher.group(3)
            } else {
                value
            }

            val evalMatcher = Pattern.compile("\\{(.*)}").matcher(_value.toString())
            result = if (evalMatcher.matches()) {
                val scriptEngine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
                scriptEngine.eval("import digital.capsa.it.json.JsonPathModifyer.shiftStartDateHours")
                scriptEngine.eval("import digital.capsa.it.json.JsonPathModifyer.shiftEndDateHours")
                scriptEngine.eval("import digital.capsa.it.json.JsonPathModifyer.alphanumericStringGenerator")
                scriptEngine.eval(evalMatcher.group(1))
            } else {
                _value
            }
            document.set(path, result)
        }
        return document.jsonString()
    }

    @Suppress("MagicNumber")
    fun shiftStartDateHours(hours: Long): Array<Int> {
        return LocalDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(hours)
                .let(LocalDateTime::toDateTimeArray)
    }

    @Suppress("MagicNumber")
    fun shiftEndDateHours(hours: Long): Array<Int> {
        return LocalDateTime.now()
                .withMinute(59)
                .withSecond(0)
                .withNano(0)
                .plusHours(hours)
                .let(LocalDateTime::toDateTimeArray)
    }


    fun alphanumericStringGenerator(length: Long): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return ThreadLocalRandom.current()
                .ints(length, 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }
}

fun LocalDateTime.toDateTimeArray(): Array<Int> = arrayOf(year, monthValue, dayOfMonth, hour, minute)
