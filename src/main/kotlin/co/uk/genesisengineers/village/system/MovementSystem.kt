package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.clock.ClockHandler
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.Movement
import co.uk.genesisengineers.village.entityComponent.Position
import java.util.ArrayList

class MovementSystem : SystemBase() {

    private val entityList = ArrayList<Entity>()

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if(entity.hasComponents(R.component.position, R.component.movement)){
                entityList.add(entity)
            }
        }
    }

    override fun update() {
        val time = ClockHandler.getInstance().getClock(ClockHandler.Type.GAME_CLOCK).time
        var position: Position
        var movement: Movement
        var elapseTime: Double
        var x: Double
        var y: Double

        for (entity in entityList) {
            position = entity.getComponent(R.component.position) as Position? ?: continue
            movement = entity.getComponent(R.component.movement) as Movement? ?: continue
            movement.lastPosition = movement.startPosition

            elapseTime = time - movement.startTime

            x = movement.startPosition.x + movement.startVelocity.x * elapseTime + 0.5 * movement.acceleration.x * elapseTime * elapseTime
            y = movement.startPosition.y + movement.startVelocity.y * elapseTime + 0.5 * movement.acceleration.y * elapseTime * elapseTime

            position.coordinates = Vector2Df(x.toFloat(), y.toFloat())
            movement.startPosition = position.coordinates
            movement.startTime = time

            x = movement.acceleration.x * elapseTime + movement.startVelocity.x
            y = movement.acceleration.y * elapseTime + movement.startVelocity.y
            movement.startVelocity = Vector2Df(x, y)
        }
    }
}
