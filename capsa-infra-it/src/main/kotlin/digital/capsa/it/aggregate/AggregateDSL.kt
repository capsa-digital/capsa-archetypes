package digital.capsa.it.aggregate

import digital.capsa.core.logger
import digital.capsa.it.TestContext
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties

interface Aggregate {
    var key: Key?

    var id: UUID?

    var parent: Aggregate?

    val children: List<Aggregate>

    fun create(context: TestContext)

    fun onCreate(context: TestContext)

    fun toString(builder: StringBuilder, nesting: Int)

    fun getChildCount(kClass: KClass<out Aggregate>): Int

    fun construct()
}

inline fun <reified T> Aggregate.getChild(i: Int): T {
    return children.filter { it is T }[i] as T
}

inline fun <reified T> Aggregate.getChild(key: Key): T {
    return children.filter { it is T && key == it.key }[0] as T
}

@DslMarker
annotation class AggregateMarker

data class Key(var value: String)

@AggregateMarker
abstract class AbstractAggregate(private val name: String) : Aggregate {

    override var key: Key? = null

    override var id: UUID? = null

    override var parent: Aggregate? = null

    override val children = arrayListOf<Aggregate>()

    protected fun <T : Aggregate> initAggregate(aggregate: T, init: T.() -> Unit): T {
        aggregate.parent = this
        aggregate.construct()
        children.add(aggregate)
        aggregate.init()
        return aggregate
    }

    override fun create(context: TestContext) {
        if (parent == null) {
            logger.info("Aggregate Tree:\n $this")
        }
        onCreate(context)
        for (c in children) {
            c.create(context)
        }
    }

    override fun toString(builder: StringBuilder, nesting: Int) {
        builder.append("".padStart(nesting * 4) + "$name ${getAttributes()}\n")
        for (c in children) {
            c.toString(builder, nesting + 1)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        toString(builder, 0)
        return builder.toString()
    }

    fun getAttributes(): String {
        return this::class.declaredMemberProperties.filter { it.visibility == KVisibility.PUBLIC }
                .joinToString(prefix = "[", postfix = "]") { "${it.name}=${it.getter.call(this)}" }
    }

    override fun getChildCount(kClass: KClass<out Aggregate>): Int {
        return children.count { kClass.isInstance(it) }
    }

    operator fun Key.unaryPlus() {
        key = this
    }
}


