package digital.capsa.archetypes.core.aggregates

abstract class Aggregate<T : AggregateId> {

    abstract var id: T

}
