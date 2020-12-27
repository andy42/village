package co.uk.genesisengineers.village.activites.mapEditor.tilePickerFragment

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.input.MotionEvent
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.view.ImageView
import co.uk.genesisengineers.core.ui.view.RecyclerView
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.core.ui.view.ViewGroup
import co.uk.genesisengineers.village.R
import java.util.ArrayList

class TilePickerAdapter(private val context: Context) : RecyclerView.Adapter<TilePickerAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater
    private var itemList: List<Item> = ArrayList()
    private var selcted = -1
    private var onItemSelectedListener: OnItemSelectedListener? = null

    init {
        this.layoutInflater = LayoutInflater()
    }

    fun setItems(itemList: List<Item>) {
        this.itemList = itemList
        this.notifyDataSetChanged()
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(context, R.layouts.item_tile_picker_xml, parent))
    }

    override fun bindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun selectedItem(index: Int) {
        if (selcted != -1) {
            itemList[selcted].selected = false
        }
        selcted = index
        val selectedItem = itemList[selcted]
        selectedItem.selected = true

        if (onItemSelectedListener != null) {
            onItemSelectedListener!!.OnItemSelected(selectedItem)
        }
        notifyDataSetChanged()
    }

    fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView
        private val selected: ImageView
        //private var index = -1

        init {
            imageView = view.findViewById(R.id.image) as ImageView
            selected = view.findViewById(R.id.selected) as ImageView
            selected.visibility = View.INVISIBLE
            view.setOnTouchListener { event: MotionEvent, root: View ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    selectedItem(index)
                }
            }
        }

        fun bind(item: Item, index: Int) {
            imageView.setDrawableArray(DrawableManager.getInstance().getDrawable(item.textureArrayId), item.textureIndex)
            selected.visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
            this.index = index
        }
    }

    class Item(internal var textureArrayId: Int, internal var textureIndex: Int) {
        internal var selected = false
    }

    interface OnItemSelectedListener {
        fun OnItemSelected(item: Item)
    }
}
