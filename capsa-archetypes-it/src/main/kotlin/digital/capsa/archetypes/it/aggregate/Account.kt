package digital.capsa.archetypes.it.aggregate

import digital.capsa.it.aggregate.AbstractAggregate

class Account : AbstractAggregate("Account") {

    fun library(init: Library.() -> Unit) = initAggregate(Library(), init)

    fun member(init: Member.() -> Unit) = initAggregate(Member(), init)

    override fun construct() {}

    override fun onCreate() {}
}

fun account(init: Account.() -> Unit): Account {
    val businessAccount = Account()
    businessAccount.construct()
    businessAccount.init()
    return businessAccount
}

