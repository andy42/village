package co.uk.genesisengineers.village.activites.mapEditor.mapLayerFragment

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.events.EventManager
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.MapSquare
import co.uk.genesisengineers.village.events.MapLayerSelectChangeEvent
import co.uk.genesisengineers.village.events.MapLayerVisibilityChangeEvent
import java.util.ArrayList

class MapLayerPresenter(private val mapEntity: Entity) {
    private var view: MapLayerView? = null
    private val layerList = ArrayList<Layer>()
    private var selectedIndex = 0

    init {
        val mapSquare = mapEntity.getComponent(R.component.mapSquare) as MapSquare

        for (i in 0 until mapSquare.layerCount) {
            val layer = mapSquare.getLayer(i)
            layerList.add(Layer(layer.name, layer.isVisable))
        }
        selectedIndex = mapSquare.layerCount - 1
        selectLayer(selectedIndex)
        EventManager.getInstance().addEvent(MapLayerSelectChangeEvent(selectedIndex))
    }

    fun setView(view: MapLayerView) {
        this.view = view
        view.setLayers(layerList)
    }

    fun onHideLayerClick(index: Int) {
        val layer = layerList[index] ?: return
        layer.isVisable = !layer.isVisable
        view!!.setLayers(layerList)
        EventManager.getInstance().addEvent(MapLayerVisibilityChangeEvent(index, layer.isVisable))
    }

    fun onSelectLayer(index: Int) {
        selectLayer(index)
        view!!.setLayers(layerList)
        EventManager.getInstance().addEvent(MapLayerSelectChangeEvent(index))
    }

    private fun selectLayer(index: Int) {
        for (i in layerList.indices) {
            layerList[i].isSelected = i == index
        }
    }

    inner class Layer(var name: String?, var isVisable: Boolean) {
        var isSelected: Boolean = false

        init {
            this.isSelected = false
        }
    }
}
