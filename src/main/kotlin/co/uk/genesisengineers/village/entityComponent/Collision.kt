package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R

class Collision() : ComponentBase() {
    var halfDimensions = Vector2Df(1f, 1f)
        private set
    var isScreenCollision = true
    var isOffScreenCulling = true
    var isObjectCollision = true

    var collisionBoxDimension: Vector2Df
        get() = Vector2Df.multiply(this.halfDimensions, 2f)
        set(dimensions) {
            this.halfDimensions = Vector2Df.multiply(dimensions, 0.5f)
        }

    constructor(dimensions: Vector2Df) : this() {
        halfDimensions = Vector2Df.multiply(dimensions, 0.5f)
    }

    constructor(componentAttributes: ComponentAttributes) : this() {
        val dimensions = componentAttributes.getVector2Df("dimensions", Vector2Df(1, 1))
        halfDimensions = Vector2Df.multiply(dimensions, 0.5f)
    }

    init {
        this.type = R.component.Collision
    }

    override fun clone(): ComponentBase {
        return Collision(Vector2Df.multiply(halfDimensions, 2f))
    }
}
