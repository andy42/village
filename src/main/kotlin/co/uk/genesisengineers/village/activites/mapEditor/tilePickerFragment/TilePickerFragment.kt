package co.uk.genesisengineers.village.activites.mapEditor.tilePickerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.activity.Activity
import co.uk.genesisengineers.core.ui.activity.Fragment
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.core.ui.view.recyclerLayoutManager.GridLayoutManager
import co.uk.genesisengineers.village.R

class TilePickerFragment(private val drawableArrayId: Int, private val title: String) : Fragment(), TilePickerView, TilePickerAdapter.OnItemSelectedListener {

    private var recyclerView: RecyclerView? = null
    private var adapter: TilePickerAdapter? = null
    private var presenter: TilePickerPresenter? = null
    private val tilePickerTitleInterface: TilePickerTitleInterface? = null

    override fun onCreate(context: Context) {
        super.onCreate(context)
        presenter = TilePickerPresenter(drawableArrayId)
    }

    override fun onCreateView(viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater()
        return layoutInflater.inflate(activity, R.layouts.fragment_tile_picker_xml, viewGroup, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        recyclerView = view as RecyclerView

        recyclerView!!.setLayoutManager(GridLayoutManager(GridLayoutManager.VERTICAL, 8))
        adapter = TilePickerAdapter(activity)
        adapter!!.setOnItemSelectedListener(this)
        recyclerView!!.setAdapter(adapter)


        presenter!!.setView(this)
        presenter!!.onCreate(DrawableManager.getInstance())
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is TilePickerTitleInterface) {
            (activity as TilePickerTitleInterface).setTilePickerTitle(title)
        }
    }

    override fun setItems(items: List<TilePickerAdapter.Item>) {
        adapter!!.setItems(items)
    }

    override fun OnItemSelected(item: TilePickerAdapter.Item) {
        if (presenter != null) {
            presenter!!.onItemSelcted(item)
        }
    }

    interface TilePickerTitleInterface {
        fun setTilePickerTitle(title: String)
    }
}
