package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.village.R

class KeyboardController() : ComponentBase() {
    init {
        this.type = R.component.keyboardController
    }

    constructor(componentAttributes: ComponentAttributes) : this()

    override fun clone(): ComponentBase {
        return KeyboardController()
    }
}
