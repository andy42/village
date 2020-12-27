package co.uk.genesisengineers.village.events

import co.uk.genesisengineers.core.events.Event

class ValueEvent<T>(private val eventType: Int, val value: T) : Event {

    override fun getType(): Int {
        return eventType
    }
}
