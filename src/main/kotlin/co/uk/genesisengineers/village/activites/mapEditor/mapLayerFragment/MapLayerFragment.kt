package co.uk.genesisengineers.village.activites.mapEditor.mapLayerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.activity.Fragment
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.core.ui.view.recyclerLayoutManager.LinearLayoutManager
import co.uk.genesisengineers.village.R

class MapLayerFragment(private val mapEntity: Entity) : Fragment(), MapLayerView, LayerAdapter.OnHideLayerClickListener, LayerAdapter.OnSelectLayerClickListener {
    private var recyclerView: RecyclerView? = null
    private var adapter: LayerAdapter? = null
    private var presenter: MapLayerPresenter? = null

    override fun onCreate(context: Context) {
        super.onCreate(context)
        presenter = MapLayerPresenter(mapEntity)
    }

    override fun onCreateView(viewGroup: ViewGroup): View {
        val layoutInflater = LayoutInflater()
        return layoutInflater.inflate(activity, R.layouts.fragment_map_layer_xml, viewGroup, false)
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        recyclerView = view.findViewById(R.id.layerRecyclerView) as RecyclerView
        recyclerView!!.setLayoutManager(LinearLayoutManager())
        adapter = LayerAdapter(activity)
        adapter!!.setOnHideLayerClickListener(this)
        adapter!!.setOnSelectLayerClickListener(this)
        recyclerView!!.setAdapter(adapter)
        presenter!!.setView(this)
    }

    override fun setLayers(layerList: List<MapLayerPresenter.Layer>) {
        adapter!!.setItems(layerList)
    }

    override fun onHideLayerClick(index: Int) {
        presenter!!.onHideLayerClick(index)
    }

    override fun onSelectLayerClick(index: Int) {
        presenter!!.onSelectLayer(index)
    }
}
