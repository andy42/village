package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R

class Movement() : ComponentBase() {

    var startPosition = Vector2Df() //this will override position.coordinates
    var lastPosition = Vector2Df()
    var startVelocity = Vector2Df()
    var acceleration = Vector2Df()
    var startTime = 0.0

    constructor(startPosition: Vector2Df) : this() {
        this.startPosition = startPosition
    }

    constructor(startPosition: Vector2Df, startVelocity: Vector2Df, acceleration: Vector2Df) : this() {
        this.lastPosition = startPosition
        this.startPosition = startPosition
        this.startVelocity = startVelocity
        this.acceleration = acceleration
        this.startTime = 0.0
    }

    constructor(componentAttributes: ComponentAttributes) : this(
            componentAttributes.getVector2Df("startPosition", Vector2Df(1, 1)),
            componentAttributes.getVector2Df("startVelocity", Vector2Df()),
            componentAttributes.getVector2Df("acceleration", Vector2Df())
    )

    override fun clone(): ComponentBase {
        return Movement(
                startPosition.copy(),
                startVelocity.copy(),
                acceleration.copy()
        )
    }

    init {
        this.type = R.component.movement
    }

    fun resetPosition() {
        this.startPosition = this.lastPosition
    }
}
