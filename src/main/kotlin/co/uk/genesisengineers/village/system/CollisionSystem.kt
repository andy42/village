package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.CollisionBox
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.Collision
import co.uk.genesisengineers.village.entityComponent.Movement
import co.uk.genesisengineers.village.entityComponent.Position
import java.util.ArrayList

class CollisionSystem : SystemBase() {

    private val entityList = ArrayList<Entity>()

    init {
        this.type = SystemBase.Type.COLLISION
    }

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if (entity.hasComponents(R.component.position, R.component.Collision)) {
                entityList.add(entity)
            }
        }
    }

    override fun update() {
        var position: Position?
        var collision: Collision?

        for (entity in entityList) {
            position = entity.getComponent(R.component.position) as Position? ?: continue
            collision = entity.getComponent(R.component.Collision) as Collision? ?: continue
            val entityCollisionBox = CollisionBox()
            entityCollisionBox.init(position.coordinates, collision.halfDimensions)
            if (objectCollision(entity, entityCollisionBox) ) {
                val movement = entity.getComponent(R.component.movement) as Movement? ?: continue
                movement!!.resetPosition()
            }
        }
    }

    private fun objectCollision(entity: Entity, entityCollisionBox: CollisionBox): Boolean {
        var position: Position?
        var collision: Collision?

        val secondCollisionBox = CollisionBox()

        for (secondEntity in entityList) {
            if (entity.id == secondEntity.id) {
                continue
            }
            position = secondEntity.getComponent(R.component.position) as Position? ?: continue
            collision = secondEntity.getComponent(R.component.Collision) as Collision? ?: continue

            secondCollisionBox.init(position.coordinates, collision.halfDimensions)
            if (entityCollisionBox.boxCollisionTest(secondCollisionBox) ) {
                return true
            }
        }
        return false
    }
}
