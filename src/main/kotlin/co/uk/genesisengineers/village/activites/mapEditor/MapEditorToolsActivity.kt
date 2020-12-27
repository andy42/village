package co.uk.genesisengineers.village.activites.mapEditor

import co.uk.genesisengineers.core.events.EventManager
import co.uk.genesisengineers.core.ui.LayoutInflater
import co.uk.genesisengineers.core.ui.activity.Activity
import co.uk.genesisengineers.core.ui.view.View
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.events.ValueEvent

class MapEditorToolsActivity : Activity(), View.OnClickListener {
    init {
        this.open = true
    }

    override fun onCreate() {
        val layoutInflater = LayoutInflater()
        this.view = layoutInflater.inflate(this, R.layouts.activity_map_editor_tools_xml, null)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        this.setDragbar(R.id.dragBar)

        setupButton(R.id.paintButton)
        setupButton(R.id.fillButton)
        setupButton(R.id.eraserButton)

        findViewById(R.id.paintButton)!!.focus()

    }

    private fun setupButton(buttonId: Int) {
        val button = findViewById(buttonId)
        button!!.setFocusable(true)
        button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.paintButton -> EventManager.getInstance().addEvent(ValueEvent(R.id.MapToolChangeEvent, R.id.paint))
            R.id.fillButton -> EventManager.getInstance().addEvent(ValueEvent(R.id.MapToolChangeEvent, R.id.fill))
            R.id.eraserButton -> EventManager.getInstance().addEvent(ValueEvent(R.id.MapToolChangeEvent, R.id.eraser))
        }
    }
}
