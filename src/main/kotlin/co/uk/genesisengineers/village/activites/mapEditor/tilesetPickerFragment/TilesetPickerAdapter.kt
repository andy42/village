package co.uk.genesisengineers.village.activites.mapEditor.tilesetPickerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.TextView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.village.R
import java.util.ArrayList

class TilesetPickerAdapter(private val context: Context) : RecyclerView.Adapter<TilesetPickerAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater
    private var listener: OnItemSelectedListener? = null

    private var items: List<TilesetPickerPresenter.ListItem> = ArrayList<TilesetPickerPresenter.ListItem>()

    init {
        layoutInflater = LayoutInflater()
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(layoutInflater.inflate(context, R.layouts.item_title_xml, parent, false))
    }

    override fun bindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<TilesetPickerPresenter.ListItem>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView
        private var item: TilesetPickerPresenter.ListItem? = null

        init {
            titleView = view.findViewById(R.id.title) as TextView
            view.setOnClickListener { button: View -> listener?.onItemSelected(item!!) }
        }

        fun bind(item: TilesetPickerPresenter.ListItem) {
            this.item = item
            titleView.setText(item.name)
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: TilesetPickerPresenter.ListItem)
    }
}
