package digital.capsa.it.dsl

import assertk.assertAll

typealias given<S> = Given<S>

class Given<S>(private val setup: () -> S) {

    fun <R> on(init: S.() -> R): On<R> =
            On { setup().init() }
}

class On<R>(private val result: () -> R) {

    fun then(assert: Then.(R) -> Unit) {
        val assertions = Then()
        assertAll {
            assertions.assert(result())
        }
    }
}

class Then
