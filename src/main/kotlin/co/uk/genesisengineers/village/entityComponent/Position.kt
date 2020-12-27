package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R

class Position() : ComponentBase() {
    var coordinates = Vector2Df()
    var rotation = 0f

    constructor(x: Float, y: Float) : this() {
        coordinates.x = x
        coordinates.y = y
    }

    constructor(x: Float, y: Float, rotation: Float) : this() {
        this.coordinates.x = x
        this.coordinates.y = y
        this.rotation = rotation
    }

    constructor(coordinates: Vector2Df, rotation: Float) : this() {
        this.coordinates = coordinates
        this.rotation = rotation
    }

    constructor(rotation: Float) : this() {
        this.rotation = rotation
    }

    constructor(componentAttributes: ComponentAttributes) : this() {
        this.coordinates = componentAttributes.getVector2Df("coordinates", coordinates)
        this.rotation = componentAttributes.getFloat("rotation", 0f)!!
    }

    override fun clone(): ComponentBase {
        return Position(
                coordinates.copy(),
                rotation
        )
    }

    init {
        this.type = R.component.position
    }

}
