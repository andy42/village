package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R

class Select() : ComponentBase() {
    var dimensions = Vector2Df()
    var textureId = 0
    var textureRegionIndex = 0
    var isSelected = false

    init {
        this.type = R.component.select
    }

    constructor(dimensions: Vector2Df, textureId: Int, textureRegionIndex: Int, selected: Boolean) : this() {
        this.dimensions = dimensions
        this.textureId = textureId
        this.textureRegionIndex = textureRegionIndex
        this.isSelected = selected
    }

    constructor(componentAttributes: ComponentAttributes) : this(
            componentAttributes.getVector2Df("dimensions", Vector2Df(1, 1)),
            componentAttributes.getIntValue("textureId", 0)!!,
            componentAttributes.getIntValue("textureRegionIndex", 0)!!,
            componentAttributes.getBoolean("selected", false)!!
    ) {
    }

    override fun clone(): ComponentBase {
        return Select(
                dimensions.copy(),
                textureId,
                textureRegionIndex,
                isSelected
        )
    }
}
