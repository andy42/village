package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R

class BasicDrawable() : ComponentBase() {

    var dimensions = Vector2Df()
    var rotation: Float = 0.toFloat()
    var drawableId: Int = 0

    init {
        this.type = R.component.BasicDrawable
    }

    constructor(dimensions: Vector2Df, drawableId: Int, rotation: Float) : this() {
        this.dimensions = dimensions
        this.drawableId = drawableId
        this.rotation = rotation
    }

    constructor(context: Context, componentAttributes: ComponentAttributes) : this() {
        this.dimensions = componentAttributes.getVector2Df("dimensions", Vector2Df(1, 1))
        this.drawableId = context.resources.getAssetId(componentAttributes.getStringValue("drawableId", ""))
        this.rotation = componentAttributes.getFloat("", 0f)!!
    }

    override fun clone(): ComponentBase {
        return BasicDrawable(dimensions.copy(), drawableId, rotation)
    }
}
