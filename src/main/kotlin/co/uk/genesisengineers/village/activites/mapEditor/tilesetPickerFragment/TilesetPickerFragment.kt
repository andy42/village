package co.uk.genesisengineers.village.activites.mapEditor.tilesetPickerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.activity.Activity
import co.uk.genesisengineers.core.ui.activity.Fragment
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.core.ui.view.recyclerLayoutManager.LinearLayoutManager
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.activites.mapEditor.OpenTilePickerFragmentForDrawableInterface

class TilesetPickerFragment : Fragment(), TilesetPickerView, TilesetPickerAdapter.OnItemSelectedListener {

    private var presenter: TilesetPickerPresenter? = null
    private var adapter: TilesetPickerAdapter? = null
    private var recyclerView: RecyclerView? = null

    private var openTilePickerFragmentForDrawableInterface: OpenTilePickerFragmentForDrawableInterface? = null

    override fun onCreate(context: Context) {
        super.onCreate(context)
        presenter = TilesetPickerPresenter()
    }

    override fun onCreateView(viewGroup: ViewGroup): View {
        val inflater = LayoutInflater()
        return inflater.inflate(activity, R.layouts.fragment_tile_set_picker_xml, viewGroup, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)

        recyclerView = view as RecyclerView
        recyclerView!!.setLayoutManager(LinearLayoutManager(LinearLayoutManager.VERTICAL))
        adapter = TilesetPickerAdapter(activity)
        adapter!!.setOnItemSelectedListener(this)
        recyclerView!!.setAdapter(adapter)

        presenter!!.setView(this)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        if (activity is TileSetPickerInterface) {
            (activity as TileSetPickerInterface).setTileSetPickerTitle("Tile sets")
        }

        if (activity is OpenTilePickerFragmentForDrawableInterface == false) {
            throw RuntimeException("parent activity of TilesetPickerFragment most implement OpenTilePickerFragmentForDrawableInterface")
        }

        openTilePickerFragmentForDrawableInterface = activity as OpenTilePickerFragmentForDrawableInterface
    }

    override fun setItems(items: List<TilesetPickerPresenter.ListItem>) {
        adapter!!.setItems(items)
    }

     override fun onItemSelected(item: TilesetPickerPresenter.ListItem) {
        presenter?.onItemSelected(item)
    }

    override fun openTilePickerFragmentForDrawable(drawableId: Int, name: String) {
        openTilePickerFragmentForDrawableInterface!!.onOpenTilePickerFragmentForDrawable(drawableId, name)
    }

    interface TileSetPickerInterface {
        fun setTileSetPickerTitle(title: String)
    }
}
