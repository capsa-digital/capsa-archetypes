package digital.capsa.it.aggregate

import digital.capsa.it.TestContext

class Account : AbstractAggregate("Account") {

    fun library(init: Library.() -> Unit) = initAggregate(Library(), init)

    override fun construct() {}

    override fun onCreate(context: TestContext) {}
}