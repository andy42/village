package co.uk.genesisengineers.village.activites.mapEditor.mapLayerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.drawable.Drawable
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.TextView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.village.R
import java.util.ArrayList

class LayerAdapter(private val context: Context) : RecyclerView.Adapter<LayerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater()
    private val iconHide: Drawable
    private val iconShow: Drawable
    private val backgroundSelected: Drawable
    private val background: Drawable
    private var hideLayerClickListener: OnHideLayerClickListener? = null
    private var selectLayerClickListener: OnSelectLayerClickListener? = null

    private var layerList: List<MapLayerPresenter.Layer> = ArrayList<MapLayerPresenter.Layer>()

    init {
        iconHide = DrawableManager.getInstance().getDrawable(R.textures.icon_hide_png)
        iconShow = DrawableManager.getInstance().getDrawable(R.textures.icon_hide_off_png)

        background = DrawableManager.getInstance().getDrawable(R.color.white)
        backgroundSelected = DrawableManager.getInstance().getDrawable(R.color.red)
    }

    fun setOnHideLayerClickListener(listener: OnHideLayerClickListener) {
        this.hideLayerClickListener = listener
    }

    fun setOnSelectLayerClickListener(listener: OnSelectLayerClickListener) {
        this.selectLayerClickListener = listener
    }


    override fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(layoutInflater.inflate(context, R.layouts.item_map_layer_xml, parent, false))
    }

    override fun bindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, layerList[position])
    }

    override fun getItemCount(): Int {
        return layerList.size
    }

    fun setItems(layerList: List<MapLayerPresenter.Layer>) {
        this.layerList = layerList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val container: View?
        private val nameView: TextView
        private val hideView: View?
        private val selectView: View?
        //private var index = -1

        init {
            container = view.findViewById(R.id.container)
            nameView = view.findViewById(R.id.nameView) as TextView
            hideView = view.findViewById(R.id.hideView)
            hideView!!.setOnClickListener { button: View -> hideLayerClickListener!!.onHideLayerClick(index) }
            selectView = view.findViewById(R.id.selectView)
            selectView!!.setOnClickListener { button: View -> selectLayerClickListener!!.onSelectLayerClick(index) }
        }

        fun bind(index: Int, layer: MapLayerPresenter.Layer) {
            this.index = index
            nameView.setText(layer.name)
            hideView!!.background = if (layer.isVisable) iconShow else iconHide
            container!!.background = if (layer.isSelected) backgroundSelected else background
        }
    }

    interface OnHideLayerClickListener {
        fun onHideLayerClick(index: Int)
    }

    interface OnSelectLayerClickListener {
        fun onSelectLayerClick(index: Int)
    }
}
