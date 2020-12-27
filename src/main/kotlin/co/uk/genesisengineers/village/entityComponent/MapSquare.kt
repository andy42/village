package co.uk.genesisengineers.village.entityComponent

import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.drawable.DrawableArray
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.core.util.Vector2Di
import co.uk.genesisengineers.village.R
import java.util.ArrayList

class MapSquare() : ComponentBase() {

    var boardDimensions = Vector2Di(1, 1)
        private set
    var tileDimensions = Vector2Df(1, 1)
        private set
    private var drawableArray: DrawableArray? = null

    private val layerArray = ArrayList<MapLayer>()

    val halfTileDimensions: Vector2Df
        get() = this.tileDimensions.multiply(Vector2Df(0.5f, 0.5f))

    val layerCount: Int
        get() = layerArray.size

    init {
        this.type = R.component.mapSquare
    }

    fun setLayerVisibility(layerIndex: Int, visable: Boolean) {
        if (layerIndex >= 0 && layerIndex < layerArray.size) {
            layerArray[layerIndex].isVisable = visable
        }
    }

    fun isMapLayerVisable(layerIndex: Int): Boolean {
        return if (layerIndex >= 0 && layerIndex < layerArray.size) {
            layerArray[layerIndex].isVisable
        } else false
    }

    constructor(drawableArray: DrawableArray, boardDimensions: Vector2Di, tileDimensions: Vector2Df) : this() {

        this.boardDimensions = boardDimensions
        this.tileDimensions = tileDimensions
        this.drawableArray = drawableArray

        init()
    }

    constructor(drawableArrayId: Int, boardDimensions: Vector2Di, tileDimensions: Vector2Df) : this() {
        val drawableArray = DrawableManager.getInstance().getDrawable(drawableArrayId) as DrawableArray

        this.boardDimensions = boardDimensions
        this.tileDimensions = tileDimensions
        this.drawableArray = drawableArray
        init()
    }

    constructor(componentAttributes: ComponentAttributes) : this(
            componentAttributes.getIntValue("drawableArrayId", -1)!!,
            componentAttributes.getVector2Di("boardDimensions", Vector2Di(1, 1)),
            componentAttributes.getVector2Df("tileDimensions", Vector2Df(1, 1))
    )

    fun init() {
        var mapLayer = MapLayer()
        mapLayer.name = "floor"
        mapLayer.init()
        for (i in 0 until mapLayer.tileArraySize) {
            mapLayer.getMapTile(i)[drawableArray] = 0
        }
        layerArray.add(mapLayer)

        mapLayer = MapLayer()
        mapLayer.name = "walls"
        mapLayer.init()
        for (i in 0 until mapLayer.tileArraySize) {
            mapLayer.getMapTile(i)[null] = 0
        }
        layerArray.add(mapLayer)
    }

    override fun clone(): ComponentBase {
        return MapSquare(drawableArray!!, boardDimensions.copy(), tileDimensions.copy())
    }

    fun getLayer(index: Int): MapLayer {
        return layerArray[index]
    }

    fun setTileTexture(position: Vector2Df, layerIndex: Int, drawableArray: DrawableArray?, drawableIndex: Int) {
        val mapLayer = layerArray[layerIndex] ?: return

        val tile = mapLayer.getMapTile(position) ?: return
        tile.drawableArray = drawableArray
        tile.drawableIndex = drawableIndex
    }

    fun setAllTileTextures(layerIndex: Int, drawableArray: DrawableArray?, drawableIndex: Int) {
        val mapLayer = layerArray[layerIndex] ?: return
        mapLayer.setAllTileTextures(drawableArray, drawableIndex)
    }

    fun getMapTile(x: Int, y: Int, layerIndex: Int): MapTile? {
        val mapLayer = layerArray[layerIndex] ?: return null
        return mapLayer.getMapTile(x + y * boardDimensions.x.toInt())
    }

    fun getMapTile(position: Vector2Df, layerIndex: Int): MapTile? {
        return getMapTile(position.x.toInt(), position.y.toInt(), layerIndex)
    }

    class MapTile(var drawableArray: DrawableArray?, drawableIndex: Int) {
        var drawableIndex = 0

        init {
            this.drawableIndex = drawableIndex
        }

        operator fun set(drawableArray: DrawableArray?, drawableIndex: Int) {
            this.drawableArray = drawableArray
            this.drawableIndex = drawableIndex
        }
    }

    inner class MapLayer {
        var isVisable = true
        var name = ""
        private val mapArray = ArrayList<MapTile>()

        val tileArraySize: Int
            get() = mapArray.size

        fun init() {
            val tileCount = (boardDimensions.x * boardDimensions.y).toInt()
            for (i in 0 until tileCount) {
                mapArray.add(MapTile(null, 0))
            }
        }

        fun getMapTile(index: Int): MapTile {
            return mapArray[index]
        }

        fun getMapTile(x: Int, y: Int): MapTile {
            return mapArray[x + y * boardDimensions.x.toInt()]
        }

        fun getMapTile(position: Vector2Df): MapTile? {
            return getMapTile(position.x.toInt(), position.y.toInt())
        }

        fun setTileTexture(position: Vector2Df, drawableArray: DrawableArray, drawableIndex: Int) {
            val tile = getMapTile(position) ?: return
            tile.drawableArray = drawableArray
            tile.drawableIndex = drawableIndex
        }

        fun setAllTileTextures(drawableArray: DrawableArray?, drawableIndex: Int) {
            for (i in mapArray.indices) {
                mapArray[i][drawableArray] = drawableIndex
            }
        }
    }
}
