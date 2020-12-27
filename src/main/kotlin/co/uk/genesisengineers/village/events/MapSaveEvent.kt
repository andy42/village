package co.uk.genesisengineers.village.events

import co.uk.genesisengineers.core.events.Event
import co.uk.genesisengineers.village.R

class MapSaveEvent(val filePath: String) : Event {

    override fun getType(): Int {
        return R.id.MapSaveEvent
    }
}
