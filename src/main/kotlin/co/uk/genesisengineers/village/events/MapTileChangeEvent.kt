package co.uk.genesisengineers.village.events

import co.uk.genesisengineers.core.events.Event
import co.uk.genesisengineers.village.R

class MapTileChangeEvent(val drawableArrayId: Int, val drawableArrayIndex: Int) : Event {

    override fun getType(): Int {
        return R.id.MapTileChange
    }
}
