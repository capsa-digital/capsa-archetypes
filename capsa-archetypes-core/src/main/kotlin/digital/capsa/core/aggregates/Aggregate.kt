package digital.capsa.core.aggregates

abstract class Aggregate<T : AggregateId> {

    abstract var id: T

}
