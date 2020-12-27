package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.input.MotionEvent
import co.uk.genesisengineers.core.system.MotionEventListener
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.CollisionBox
import co.uk.genesisengineers.core.visualisation.Visualisation
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.Collision
import co.uk.genesisengineers.village.entityComponent.Position
import co.uk.genesisengineers.village.entityComponent.Select
import com.sun.javafx.geom.Vec3f
import java.util.ArrayList

class MouseSelectSystem : SystemBase(), MotionEventListener {

    private val entityList = ArrayList<Entity>()

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if(entity.hasComponents(R.component.position, R.component.select, R.component.Collision)){
                entityList.add(entity)
            }
        }
    }

    override fun update() {
        val visualisation = Visualisation.getInstance()
        visualisation.useColourProgram()

        var position: Position? = null
        var select: Select? = null

        for (entity in entityList) {
            position = entity.getComponent(R.component.position) as Position? ?: continue
            select = entity.getComponent(R.component.select) as Select? ?: continue

            if (select.isSelected) {
                visualisation.drawColouredSquare(Vec3f(0f, 0f, 0f), position.coordinates, select.dimensions, 0f)
            }
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {

        var position: Position
        var select: Select
        var collision: Collision

        for (entity in entityList) {
            position = entity.getComponent(R.component.position) as Position? ?: continue
            select = entity.getComponent(R.component.select) as Select? ?: continue
            collision = entity.getComponent(R.component.Collision) as Collision? ?: continue

            val entityCollisionBox = CollisionBox()
            entityCollisionBox.init(position.coordinates, collision.halfDimensions)
            select.isSelected = entityCollisionBox.pointCollisionTest(motionEvent.position)
        }

        return false
    }
}
