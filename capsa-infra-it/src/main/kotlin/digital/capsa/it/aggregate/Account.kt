package digital.capsa.it.aggregate

class Account : AbstractAggregate("Account") {

    fun library(init: Library.() -> Unit) = initAggregate(Library(), init)

    override fun construct() {}

    override fun onCreate(context: AggregateBuilderContext) {}
}