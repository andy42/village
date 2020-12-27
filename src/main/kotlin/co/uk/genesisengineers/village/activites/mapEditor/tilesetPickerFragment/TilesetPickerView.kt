package co.uk.genesisengineers.village.activites.mapEditor.tilesetPickerFragment

interface TilesetPickerView {
    fun setItems(items: List<TilesetPickerPresenter.ListItem>)
    fun openTilePickerFragmentForDrawable(drawableId: Int, name: String)
}
