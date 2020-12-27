package co.uk.genesisengineers.village

import co.uk.genesisengineers.core.clock.ClockHandler
import co.uk.genesisengineers.core.content.entityPrototypeFactory.EntityPrototypeFactory
import co.uk.genesisengineers.core.drawable.DrawableManager
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.EntityHandler
import co.uk.genesisengineers.core.entity.EntityPool
import co.uk.genesisengineers.core.events.EventManager
import co.uk.genesisengineers.core.input.KeyMapper
import co.uk.genesisengineers.core.input.Mouse
import co.uk.genesisengineers.core.shape.ShapeManager
import co.uk.genesisengineers.core.system.SystemHandler
import co.uk.genesisengineers.core.ui.ActivityManager
import co.uk.genesisengineers.core.util.Logger
import co.uk.genesisengineers.core.util.Vector2Df
import co.uk.genesisengineers.core.util.Vector2Di
import co.uk.genesisengineers.core.visualisation.MainWindow
import co.uk.genesisengineers.core.visualisation.TextureManager
import co.uk.genesisengineers.core.visualisation.Visualisation
import co.uk.genesisengineers.village.activites.mapEditor.MapEditorActivity
import co.uk.genesisengineers.village.activites.mapEditor.MapEditorToolsActivity
import co.uk.genesisengineers.village.entityComponent.MapSquare
import co.uk.genesisengineers.village.entityComponent.Position
import co.uk.genesisengineers.village.entityComponent.factory.EntityPrototypeFactoryJSON
import co.uk.genesisengineers.village.system.*

import java.io.File
import java.util.Date
import java.util.List

import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW
import org.lwjgl.util.nfd.NativeFileDialog
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.util.nfd.NativeFileDialog.*

class App : MainWindow.OnWindowCloseListener{
    private val mainWindow =  MainWindow()
    private val systemHandler = SystemHandler()
    private val entityHandler = EntityHandler()
    private lateinit var activityManager : ActivityManager
    private val applicationContext = ApllicationContext()

    private val shapeManager = ShapeManager()
    private lateinit var drawableManager: DrawableManager
    private lateinit var entityPrototypeFactory : EntityPrototypeFactory

    private var lastUpdateTime = getCurrentTime()
    private var sleepTime = 0

    val windowWidth = 1200
    val windowHeight = 800

    fun start(args: Array<String>){
        mainWindow.setOnWindowCloseListener(this)

        if(!init()){
            System.exit(0)
        }
        worldInit()
        loop()
    }

    private fun init() : Boolean{
        Logger.debug("debug messages enabled")
        activityManager = ActivityManager.createInstance(applicationContext)

        if (!mainWindow.init(windowWidth, windowHeight)) {
            return false
        }

        // key mapper / handler
        val keyMapper = KeyMapper.getInstance()
        keyMapper.init(mainWindow.window)

        // mouse handler
        val mouse = Mouse.getInstance()
        mouse.init(mainWindow.window)

        // visualisation
        val visualisation = Visualisation.getInstance()
        visualisation.init(mainWindow.window)
        visualisation.setWindowDimensions(windowWidth, windowHeight)

        TextureManager.getInstance().load(
                applicationContext,
                applicationContext.resources.getAssetsOfType(R.textures.TYPE)
        )

        Visualisation.getInstance().loadFont(applicationContext, R.fonts.arial_png, R.fonts.arial_fnt)

        applicationContext.resources.loadColors(applicationContext, R.values.colors_json)

        shapeManager.loadShapes(applicationContext,
                applicationContext.resources.getAssetsOfType(R.shapes.TYPE))

        drawableManager = DrawableManager.createInstance(shapeManager, TextureManager.getInstance())
        drawableManager.load(
                applicationContext,
                applicationContext.resources.getAssetsOfType(R.drawables.TYPE))

        drawableManager.createColorDrawables(
                applicationContext.resources.colorList,
                shapeManager.getShape(R.shapes.square_top_left_json))

        drawableManager.createTextureDrawables(
                TextureManager.getInstance().textures,
                shapeManager.getShape(R.shapes.square_top_left_json))

        entityPrototypeFactory = EntityPrototypeFactoryJSON(applicationContext)
        entityPrototypeFactory.loadEntities(
                applicationContext.resources.getAssetFileAsString(R.entities.entityList_json))

        return true
    }

    private fun worldInit() {

        entityHandler.addEntity(entityPrototypeFactory.cloneEntity("player"))

        val entity: Entity

        entity = entityHandler.createEntity()
        entity.addComponent(Position(0f, 0f))
        entity.addComponent(MapSquare(R.drawables.tiles_json, Vector2Di(20, 20), Vector2Df(64, 64)))
        entity.id


        systemHandler.addSystem(KeyboardControllerSystem())
        systemHandler.addSystem(MovementSystem())
        systemHandler.addSystem(CollisionSystem())
        systemHandler.addSystem(MapRenderSystem())
        systemHandler.addSystem(RenderDrawableSystem(drawableManager))
        systemHandler.addSystem(MouseSelectSystem())
        systemHandler.addSystem(MapEditorSystem())

        systemHandler.init(entityHandler)

        ActivityManager.getInstance().addActivity(MapEditorActivity(entity))
        ActivityManager.getInstance().addActivity(MapEditorToolsActivity())
    }

    private fun checkResult(result: Int, path: PointerBuffer) {
        when (result) {
            NFD_OKAY -> {
                println("Success!")
                println(path.getStringUTF8(0))
                nNFDi_Free(path.get(0))
            }
            NFD_CANCEL -> println("User pressed cancel.")
            else // NFD_ERROR
            -> System.err.format("Error: %s\n", NativeFileDialog.NFD_GetError())
        }
    }


    private fun loop(){
        val visualisation = Visualisation.getInstance()

        lastUpdateTime = getCurrentTime()

        //EventManager.getInstance().addEvent(EventBasic(R.id.GameReset))

        while (!mainWindow.shouldClose()) {
            sleepTime = calculateThreadSleepTime()

            ClockHandler.getInstance().update()

            visualisation.setWindowDimensions(mainWindow.width, mainWindow.height)
            visualisation.initProjection()

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT) // clear the framebuffer

            visualisation.clearMatrixStack()

            var motionEvent = Mouse.getInstance().nextMotionEvent
            while (motionEvent != null) {
                if (!ActivityManager.getInstance().dispatchTouchEvent(motionEvent)) {
                    systemHandler.dispatchTouchEvent(motionEvent)
                }
                motionEvent = Mouse.getInstance().nextMotionEvent
            }

            KeyMapper.getInstance().update()
            var keyEvent = KeyMapper.getInstance().nextKeyEvent
            while (keyEvent != null) {
                if (!ActivityManager.getInstance().dispatchKeyEvent(keyEvent)) {
                    systemHandler.dispatchKeyEvent(keyEvent);
                }
                keyEvent = KeyMapper.getInstance().nextKeyEvent
            }

            var event = EventManager.getInstance().next
            while (event != null) {
                systemHandler.dispatchEvent(event)
                event = EventManager.getInstance().next
            }

            systemHandler.update()

            ActivityManager.getInstance().update()
            ActivityManager.getInstance().renderActivityList()

            visualisation.finalise()

            GLFW.glfwPollEvents()

            if (sleepTime > 0) sleepThread(sleepTime)
        }
    }

    private fun getCurrentTime(): Long {
        return Date().time
    }

    private fun calculateThreadSleepTime(): Int {
        val currentTime = getCurrentTime()
        val timeDiff = currentTime - lastUpdateTime
        lastUpdateTime = currentTime

        return if (timeDiff < 16) {
            Math.toIntExact(timeDiff - 16)
        } else {
            0
        }
    }

    private fun sleepThread(time: Int) {
        try {
            Thread.sleep(time.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    override fun onWindowClose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


fun main(args: Array<String>) {
    println("Hello World!")
    App().start(args)
}