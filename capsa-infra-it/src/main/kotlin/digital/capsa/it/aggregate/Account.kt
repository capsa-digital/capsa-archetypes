package digital.capsa.it.aggregate

import org.apache.commons.lang3.RandomStringUtils
import java.util.Locale

class Account(
        var domain: String? = null,
        var password: String? = null,
        var lang: Locale? = null
) : AbstractAggregate("Account") {

    fun library(init: Library.() -> Unit) = initAggregate(Library(), init)

    override fun construct() {
        lang = lang ?: Locale.ENGLISH
        domain = domain ?: RandomStringUtils.randomNumeric(10)
    }

    override fun onCreate(context: AggregateBuilderContext) {}
}