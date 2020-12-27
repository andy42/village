package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.visualisation.Visualisation
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.BasicDrawable
import co.uk.genesisengineers.village.entityComponent.Position
import java.util.ArrayList

class RenderDrawableSystem(private val drawableManager: DrawableManager) : SystemBase() {
    private val entityList = ArrayList<Entity>()

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if(entity.hasComponents(R.component.position, R.component.BasicDrawable)){
                entityList.add(entity)
            }
        }
    }

    override fun update() {
        val visualisation = Visualisation.getInstance()
        visualisation.useTextureProgram()


        var position: Position?
        var basicDrawable: BasicDrawable?
        for (entity in entityList) {
            position = entity.getComponent(R.component.position) as Position? ?: continue
            basicDrawable = entity.getComponent(R.component.BasicDrawable) as BasicDrawable? ?: continue

            drawableManager.draw(basicDrawable.drawableId, position.coordinates, basicDrawable.dimensions, basicDrawable.rotation)
        }
    }

}
