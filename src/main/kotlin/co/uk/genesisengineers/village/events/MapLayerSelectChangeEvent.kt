package co.uk.genesisengineers.village.events

import co.uk.genesisengineers.core.events.Event
import co.uk.genesisengineers.village.R

class MapLayerSelectChangeEvent(var layerIndex: Int) : Event {

    override fun getType(): Int {
        return R.id.MapLayerChange
    }
}
