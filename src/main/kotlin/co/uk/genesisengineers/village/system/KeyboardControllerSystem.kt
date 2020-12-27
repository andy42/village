package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.input.KeyEvent
import co.uk.genesisengineers.core.system.KeyEventListener
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.Movement
import java.util.ArrayList

class KeyboardControllerSystem : SystemBase(), KeyEventListener {

    private var moveLeft = false
    private var moveRight = false
    private var moveUp = false
    private var moveDown = false

    private val entityList = ArrayList<Entity>()

    init {
        this.type = SystemBase.Type.KEYBOARD_CONTROLLER
    }

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if (entity.hasComponents(R.component.keyboardController, R.component.movement) ) {
                entityList.add(entity)
            }
        }
    }

    private fun axisUpdater(positiveState: Boolean, negativeState: Boolean, maxValue: Int): Int {
        if (positiveState == true) {
            return if (negativeState == true) 0 else maxValue
        } else if (positiveState == false) {
            return if (negativeState == false) 0 else -maxValue
        }
        return -maxValue
    }

    override fun update() {
        var movement: Movement

        for (entity in entityList) {
            movement = entity.getComponent(R.component.movement) as Movement? ?: continue
            val velocity = Vector2Df()
            velocity.x = axisUpdater(moveRight, moveLeft, 100).toFloat()
            velocity.y = axisUpdater(moveDown, moveUp, 100).toFloat()
            movement.startVelocity = velocity
        }
    }

    private fun updateKey(keyEvent: KeyEvent, matchKey: Int, currentValue: Boolean): Boolean {
        return if (keyEvent.keyValue == matchKey && keyEvent.action == KeyEvent.ACTION_DOWN) {
            true
        } else if (keyEvent.keyValue == matchKey && keyEvent.action == KeyEvent.ACTION_UP) {
            false
        } else {
            currentValue
        }
    }

    override fun dispatchKeyEvent(keyEvent: KeyEvent): Boolean {

        this.moveUp = updateKey(keyEvent, 119, this.moveUp)
        this.moveLeft = updateKey(keyEvent, 97, this.moveLeft)
        this.moveDown = updateKey(keyEvent, 115, this.moveDown)
        this.moveRight = updateKey(keyEvent, 100, this.moveRight)

        return false
    }
}
