package digital.capsa.it.aggregate

import digital.capsa.it.TestContext

class Account : AbstractAggregate("Account") {

    fun library(init: Library.() -> Unit) = initAggregate(Library(), init)

    fun member(init: Member.() -> Unit) = initAggregate(Member(), init)

    override fun construct() {}

    override fun onCreate(context: TestContext) {}
}