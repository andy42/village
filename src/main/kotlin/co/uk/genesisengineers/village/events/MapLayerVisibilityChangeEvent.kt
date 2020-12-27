package co.uk.genesisengineers.village.events

import co.uk.genesisengineers.core.events.Event
import co.uk.genesisengineers.village.R

class MapLayerVisibilityChangeEvent(val layerIndex: Int, val isVisibility: Boolean) : Event {

    override fun getType(): Int {
        return R.id.MapLayerVisibilityChange
    }
}
