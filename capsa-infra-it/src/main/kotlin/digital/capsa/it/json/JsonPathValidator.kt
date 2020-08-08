package digital.capsa.it.json

import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SigningKeyResolverAdapter
import net.minidev.json.JSONArray
import org.hamcrest.Matchers
import org.jetbrains.kotlin.cli.common.environment.setIdeaIoUseFallback
import org.junit.Assert
import java.security.Key
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

@Suppress("TooGenericExceptionThrown")
object JsonPathValidator {

    init {
        setIdeaIoUseFallback()
    }

    @Suppress("UnsafeCast", "ComplexMethod", "LongMethod", "NestedBlockDepth", "ThrowsCount")
    fun assertJson(json: String, rules: List<ValidationRule>, properties: Map<String, String?>? = null) {
        val scriptEngine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
        scriptEngine.eval("import digital.capsa.it.json.JsonPathValidator.getShiftedStartDate")
        scriptEngine.eval("import digital.capsa.it.json.JsonPathValidator.getShiftedEndDate")
        scriptEngine.eval("import digital.capsa.it.json.JsonPathValidator.getShiftedTriggerDate")

        val document = Configuration.defaultConfiguration().jsonProvider().parse(json)

        for (rule in rules) {
            val valueList: List<Any> = if (rule.value is String) {
                val propertyMatcher = Pattern.compile("(.*)\\[\\[(.*)]](.*)").matcher(rule.value.toString())
                val _value = if (properties != null && propertyMatcher.matches()) {
                    propertyMatcher.group(1) +
                            properties[propertyMatcher.group(2)].toString() +
                            propertyMatcher.group(3)
                } else {
                    rule.value.toString()
                }
                val evalMatcher = Pattern.compile("\\{(.*)}").matcher(_value)
                if (evalMatcher.matches()) {
                    val eval = scriptEngine.eval(evalMatcher.group(1))
                    if (eval is List<*>) {
                        @Suppress("UNCHECKED_CAST")
                        eval as List<Any>
                    } else {
                        listOf(eval)
                    }
                } else if (_value != "") {
                    _value.trim().split(",\\s*".toRegex())
                } else {
                    emptyList()
                }
            } else if (rule.value is List<*>) {
                @Suppress("UNCHECKED_CAST")
                rule.value as List<Any>
            } else {
                throw Error("Can't evaluate ${rule.value}")
            }
            var jsonPath: Any?
            try {
                jsonPath = JsonPath.read(document, rule.jsonPath)
            } catch (e: Exception) {
                throw Error("Path not found, document: $document, path: ${rule.jsonPath}", e)
            }
            if (jsonPath == null) {
                Assert.assertNull(
                        "json path ${rule.jsonPath} validation failed, document: $document", valueList[0])
            } else
                when (jsonPath) {
                    is JSONArray -> {
                        if (rule.op != OpType.equal) {
                            throw Error("'${rule.op}' op is not supported for JSONArray result. Use 'equal' op")
                        }
                        if (jsonPath.size == 0) {
                            Assert.assertEquals("json path ${rule.jsonPath} validation failed, document: $document", valueList, emptyList<Any>())
                        } else {
                            Assert.assertEquals(
                                    "json path ${rule.jsonPath} validation failed, document: $document", valueList.toSet(), jsonPath.toSet())
                        }
                    }
                    is Number -> {
                        if (rule.op != OpType.equal) {
                            throw Error("${rule.op} op is not supported for JSONArray result. Use 'equal' op")
                        }
                        Assert.assertEquals(
                                "json path ${rule.jsonPath} validation failed, document: $document", valueList[0], jsonPath)
                    }
                    is String ->
                        when (rule.op) {
                            OpType.regex ->
                                Assert.assertThat(
                                        "json path ${rule.jsonPath} validation failed, document: $document", jsonPath, Matchers.matchesPattern(valueList[0].toString()))
                            OpType.equal ->
                                Assert.assertEquals(
                                        "json path ${rule.jsonPath} validation failed, document: $document", valueList[0], jsonPath)
                            OpType.like ->
                                Assert.assertThat(
                                        "json path ${rule.jsonPath} validation failed, document: $document", jsonPath, Matchers.matchesPattern(Pattern.compile(".*${valueList[0]}.*", Pattern.DOTALL)))
                            OpType.jwt ->
                                verifyJwtPayload(jsonPath, valueList[0].toString(), valueList[1].toString())
                        }
                    else -> throw Error("Expected JSONArray, received ${jsonPath.let { it::class }}")
                }
        }
    }

    @Suppress("MagicNumber")
    fun verifyJwtPayload(jwtToken: String, payloadName: String, payloadValue: String) {
        try {
            val claims: Claims = Jwts.parser()
                    .setSigningKeyResolver(object : SigningKeyResolverAdapter() {
                        override fun resolveSigningKey(header: JwsHeader<*>?, claims: Claims?): Key? {
                            Assert.assertTrue(
                                    "JWT token validation failed for $payloadName == $payloadValue, claims = $claims",
                                    claims?.get(payloadName)?.equals(payloadValue) ?: false)
                            return null // will throw exception, can be caught in caller
                        }
                    })
                    .parseClaimsJws(jwtToken).getBody()
        } catch (e: Exception) {
            // do nothing
        }
    }

    @Suppress("MagicNumber")
    fun getShiftedStartDate(hours: Long, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return OffsetDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(hours)
                .format(formatter)
    }

    @Suppress("MagicNumber")
    fun getShiftedStartDate(hours: Long): String = getShiftedStartDate(hours, "yyyy-MM-dd'T'HH:mm:ss")

    @Suppress("MagicNumber")
    fun getShiftedEndDate(hours: Long, pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return OffsetDateTime.now()
                .withMinute(59)
                .withSecond(0)
                .withNano(0)
                .plusHours(hours)
                .format(formatter)
    }

    @Suppress("MagicNumber")
    fun getShiftedEndDate(hours: Long): String = getShiftedEndDate(hours, "yyyy-MM-dd'T'HH:mm:ss")

    @Suppress("MagicNumber")
    fun getShiftedTriggerDate(hours: Long): String {
        return OffsetDateTime.now()
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .plusHours(hours).toInstant().toEpochMilli().div(1000).toString()
    }
}

data class ValidationRule(val jsonPath: String, val op: OpType, val value: Any?)

enum class OpType {
    equal, like, regex, jwt
}
