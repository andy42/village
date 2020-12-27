package co.uk.genesisengineers.village.system

import co.uk.genesisengineers.core.drawable.DrawableArray
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.events.Event
import co.uk.genesisengineers.core.input.MotionEvent
import co.uk.genesisengineers.core.system.EventListener
import co.uk.genesisengineers.core.system.MotionEventListener
import co.uk.genesisengineers.core.system.SystemBase
import co.uk.genesisengineers.core.util.Logger
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.village.R
import co.uk.genesisengineers.village.entityComponent.MapSquare
import co.uk.genesisengineers.village.entityComponent.Position
import co.uk.genesisengineers.village.events.*
import java.util.ArrayList

class MapEditorSystem : SystemBase(), EventListener, MotionEventListener {

    private var drawableArray: DrawableArray? = null
    private var drawableArrayIndex = -1
    private var selectedMapLayer = 0

    private var toolType = R.id.paint

    private val entityList = ArrayList<Entity>()

    override fun init(entityHandler: EntityHandler) {
        for (entity in entityHandler) {
            if(entity.hasComponents(R.component.position, R.component.mapSquare)){
                entityList.add(entity)
            }
        }
    }

    override fun update() {

    }

    override fun dispatchEvent(event: Event): Boolean {
        if (event.type == R.id.MapTileChange && event is MapTileChangeEvent) {
            val drawableArrayId = event.drawableArrayId
            val drawable = DrawableManager.getInstance().getDrawable(drawableArrayId)

            if (drawable is DrawableArray) {
                this.drawableArray = drawable
                drawableArrayIndex = event.drawableArrayIndex
            }
        } else if (event.type == R.id.MapLayerChange && event is MapLayerSelectChangeEvent) {
            selectedMapLayer = event.layerIndex
        } else if (event.type == R.id.MapLayerVisibilityChange && event is MapLayerVisibilityChangeEvent) {
            val mapSquare = entityList[0].getComponent(R.component.mapSquare) as MapSquare?
            mapSquare?.setLayerVisibility(event.layerIndex, event.isVisibility)
        } else if (event.type == R.id.MapLoadEvent && event is MapLoadEvent) {
            loadMap(event.filePath)
        } else if (event.type == R.id.MapSaveEvent && event is MapSaveEvent) {
            saveMap(event.filePath)
        }

        if (event.type == R.id.MapToolChangeEvent) {
            toolType = (event as ValueEvent<Int>).value
        }
        return false
    }

    private fun loadMap(filePath: String) {
        Logger.debug("loadMap : $filePath")
    }

    private fun saveMap(filePath: String) {
        Logger.debug("saveMap : $filePath")
        var mapSquare: MapSquare
        for (entity in entityList) {
            mapSquare = entity.getComponent(R.component.mapSquare) as MapSquare
        }
    }


    private fun transformToMap(worldPosition: Vector2Df, entity: Entity): Vector2Df? {
        val position = entity.getComponent(R.component.position) as Position
        val mapSquare = entity.getComponent(R.component.mapSquare) as MapSquare
        if(position == null || mapSquare == null){
            val errorMessage = "mapSquare == null"
            Logger.exception(Exception(errorMessage), errorMessage)
            return null
        }

        val relativeMapPosition = worldPosition.sub(position.coordinates)

        if (mapSquare.boardDimensions.x === 0 || mapSquare.boardDimensions.y === 0) {
            val errorMessage = "MapEditorSystem can not divide by 0  BoardDimensions x|y == 0, entity.id:" + entity.id
            Logger.exception(Exception(errorMessage), errorMessage)
            return null
        }

        return relativeMapPosition.divide(mapSquare.tileDimensions).floor()
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        var mapSquare: MapSquare

        if (motionEvent.action != MotionEvent.ACTION_DOWN && motionEvent.action != MotionEvent.ACTION_MOVE) return false

        for (entity in entityList) {
            mapSquare = entity.getComponent(R.component.mapSquare) as MapSquare

            if (drawableArray == null || drawableArrayIndex < 0) {
                continue
            }

            val mapCoordinates = transformToMap(motionEvent.position, entity) ?: continue
            if (mapCoordinates.x >= 0 &&
                    mapCoordinates.y >= 0 &&
                    mapCoordinates.x < mapSquare.boardDimensions.x &&
                    mapCoordinates.y < mapSquare.boardDimensions.y) {

                when (toolType) {
                    R.id.paint -> mapSquare.setTileTexture(mapCoordinates, selectedMapLayer, drawableArray, drawableArrayIndex)
                    R.id.fill -> mapSquare.setAllTileTextures(selectedMapLayer, drawableArray, drawableArrayIndex)
                    R.id.eraser -> mapSquare.setTileTexture(mapCoordinates, selectedMapLayer, null, 0)
                }
            }
        }
        return false
    }
}
