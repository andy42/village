package co.uk.genesisengineers.village.activites.mapEditor.tilesetPickerFragment

import co.uk.genesisengineers.village.R
import java.util.ArrayList

class TilesetPickerPresenter {

    private val list = ArrayList<ListItem>()

    private var view: TilesetPickerView? = null

    fun setView(view: TilesetPickerView) {
        this.view = view
        list.clear()
        list.add(ListItem(R.drawables.wallTiles_json, "Walls"))
        list.add(ListItem(R.drawables.floorTiles_json, "Floors"))
        list.add(ListItem(R.drawables.terrainTiles_json, "Terrain"))
        list.add(ListItem(R.drawables.tiles_json, "GTA Tiles"))
        view.setItems(list)
    }

    fun onItemSelected(item: TilesetPickerPresenter.ListItem) {
        view?.openTilePickerFragmentForDrawable(item.drawableArrayId, item.name)
    }

    inner class ListItem(var drawableArrayId: Int, var name: String)
}
