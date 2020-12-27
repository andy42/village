package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.core.visualisation.Visualisation
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.MapSquare
import co.uk.genesisengineers.village.entityComponent.Position
import java.util.ArrayList

class MapRenderSystem : SystemBase() {

    private val entityList = ArrayList<Entity>()

    init {
        this.type = SystemBase.Type.MAP_RENDER
    }

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if(entity.hasComponents(R.component.position, R.component.mapSquare)) {
                entityList.add(entity)
            }
        }
    }

    private fun updateMap(visualisation: Visualisation, entity: Entity) {
        val position = entity.getComponent(R.component.position) as Position? ?: return
        val mapSquare = entity.getComponent(R.component.mapSquare) as MapSquare? ?: return

        val mapPosition = position.coordinates.add(mapSquare.halfTileDimensions)
        var tilePosition: Vector2Df


        var mapTile: MapSquare.MapTile?
        for (layerIndex in 0 until mapSquare.layerCount) {
            if (mapSquare!!.isMapLayerVisable(layerIndex) === false) {
                continue
            }
            for (x in 0 until mapSquare.boardDimensions.x) {
                for (y in 0 until mapSquare.boardDimensions.y) {
                    mapTile = mapSquare.getMapTile(x, y, layerIndex) ?: continue
                    tilePosition = mapSquare.tileDimensions.multiply(Vector2Df(x, y)).add(mapPosition)
                    mapTile.drawableArray?.draw(tilePosition, mapSquare.tileDimensions, 0f, mapTile.drawableIndex)
                }
            }
        }
    }

    override fun update() {
        val visualisation = Visualisation.getInstance()
        visualisation.useTextureProgram()
        for (entity in entityList) {
            updateMap(visualisation, entity)
        }
    }
}