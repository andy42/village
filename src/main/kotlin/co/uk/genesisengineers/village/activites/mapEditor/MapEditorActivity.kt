package co.uk.genesisengineers.village.activites.mapEditor

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.events.EventManager
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.activity.Activity
import co.uk.genesisengineers.core.ui.view.TextView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.util.Logger
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.activites.mapEditor.mapLayerFragment.MapLayerFragment
import co.uk.genesisengineers.village.activites.mapEditor.tilePickerFragment.TilePickerFragment
import co.uk.genesisengineers.village.activites.mapEditor.tilesetPickerFragment.TilesetPickerFragment
import co.uk.genesisengineers.village.events.MapLoadEvent
import co.uk.genesisengineers.village.events.MapSaveEvent
import co.uk.genesisengineers.village.util.FileDialog

class MapEditorActivity(private val mapEntity: Entity) : Activity(), OpenTilePickerFragmentForDrawableInterface, TilePickerFragment.TilePickerTitleInterface, TilesetPickerFragment.TileSetPickerInterface, View.OnClickListener, FileDialog.FileDialogInterface {
    private var backButton: View? = null
    private var navbarTitle: TextView? = null

    init {
        this.open = true
    }

    override fun onCreate() {
        val layoutInflater = LayoutInflater()
        this.view = layoutInflater.inflate(this, R.layouts.activity_map_editor_xml, null)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        this.setDragbar(R.id.dragBar)

        this.backButton = findViewById(R.id.backButton)
        this.navbarTitle = findViewById(R.id.navbarTitle) as TextView

        fragmentManager.replace(R.id.layerContainer, MapLayerFragment(mapEntity), true)
        fragmentManager.replace(R.id.tilePickerContainer, TilesetPickerFragment(), true)
        backButton!!.visibility = View.GONE

        findViewById(R.id.backButton)!!.setOnClickListener { button: View ->
            fragmentManager.popBackStack(R.id.tilePickerContainer)
            backButton!!.visibility = View.GONE
        }

        findViewById(R.id.saveBtn)!!.setOnClickListener(this)
        findViewById(R.id.loadBtn)!!.setOnClickListener(this)
    }

    override fun onOpenTilePickerFragmentForDrawable(drawableId: Int, name: String) {
        fragmentManager.replace(R.id.tilePickerContainer, TilePickerFragment(drawableId, name), true)
        backButton!!.visibility = View.VISIBLE
    }

    override fun setTilePickerTitle(title: String) {
        navbarTitle!!.setText(title)
    }

    override fun setTileSetPickerTitle(title: String) {
        navbarTitle!!.setText(title)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loadBtn -> FileDialog(LOAD_MAP, this).showLoadDialog()
            R.id.saveBtn -> FileDialog(SAVE_MAP, this).showSaveDialog()
        }
    }

    override fun fileDialogSuccess(requestCode: Int, filePath: String) {
        when (requestCode) {
            SAVE_MAP -> saveMap(filePath)
            LOAD_MAP -> loadMap(filePath)
        }
    }

    override fun fileDialogError(requestCode: Int, error: String?) {
        Logger.error(if (requestCode == SAVE_MAP) "Save Map" else "Load Map : $error")
    }

    override fun fileDialogCancel(requestCode: Int) {}

    fun saveMap(filePath: String) {
        EventManager.getInstance().addEvent(MapSaveEvent(filePath))
    }

    fun loadMap(filePath: String) {
        EventManager.getInstance().addEvent(MapLoadEvent(filePath))
    }

    companion object {

        private val LOAD_MAP = 1
        private val SAVE_MAP = 2
    }
}
