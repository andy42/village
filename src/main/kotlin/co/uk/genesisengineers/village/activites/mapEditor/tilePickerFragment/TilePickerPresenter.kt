package co.uk.genesisengineers.village.activites.mapEditor.tilePickerFragment

import co.uk.genesisengineers.core.drawable.DrawableArray
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.events.EventManager
import co.uk.genesisengineers.village.events.MapTileChangeEvent
import java.util.ArrayList

class TilePickerPresenter(private val drawableArrayId: Int) {

    private var view: TilePickerView? = null

    fun onCreate(drawableManager: DrawableManager) {
        val items = ArrayList<TilePickerAdapter.Item>()
        val drawableArray = drawableManager.getDrawable(drawableArrayId) as DrawableArray
        for (i in 0 until drawableArray.size()) {
            items.add(TilePickerAdapter.Item(drawableArrayId, i))
        }
        view!!.setItems(items)
    }

    fun onItemSelcted(item: TilePickerAdapter.Item) {
        EventManager.getInstance().addEvent(MapTileChangeEvent(item.textureArrayId, item.textureIndex))
    }

    fun setView(view: TilePickerView) {
        this.view = view
    }
}
