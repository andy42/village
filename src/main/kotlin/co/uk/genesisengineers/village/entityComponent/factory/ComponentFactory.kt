package co.uk.genesisengineers.village.entityComponent.factory

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.content.entityPrototypeFactory.ComponentAttributes
import co.uk.genesisengineers.core.entity.component.ComponentBase
import co.uk.genesisengineers.core.util.Logger
import co.uk.genesisengineers.village.entityComponent.*
import java.lang.reflect.Constructor
import java.util.HashMap

class ComponentFactory(private val context: Context) {
    private val mConstructorArgs = arrayOfNulls<Any>(1)

    private fun verifyClassLoader(constructor: Constructor<out ComponentBase>): Boolean {
        val constructorLoader = constructor.declaringClass.classLoader
        var cl: ClassLoader? = javaClass.classLoader
        do {
            if (constructorLoader === cl) {
                return true
            }
            cl = cl!!.parent
        } while (cl != null)
        return false
    }

    private fun createComponentFromClassName(className: String, attrs: ComponentAttributes): ComponentBase? {
        var constructor: Constructor<out ComponentBase>? = sConstructorMap[className]
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null
            sConstructorMap.remove(className)
        }

        var clazz: Class<out ComponentBase>? = null

        try {
            if (constructor == null) {
                clazz = javaClass.classLoader.loadClass(className).asSubclass(ComponentBase::class.java)
                constructor = clazz!!.getConstructor(*mConstructorSignature)
                constructor!!.isAccessible = true
                sConstructorMap[className] = constructor
            }

            val args = mConstructorArgs
            args[0] = attrs

            return constructor.newInstance(*args)

        } catch (e: Exception) {
            Logger.exception(e, e.message)
        }

        return null
    }

    fun createComponent(componentType: String, attrs: ComponentAttributes): ComponentBase? {
        when (componentType) {
            "BasicDrawable" -> return BasicDrawable(context, attrs)
            "Collision" -> return Collision(attrs)
            "KeyboardController" -> return KeyboardController(attrs)
            "MapSquare" -> return MapSquare(attrs)
            "Movement" -> return Movement(attrs)
            "Position" -> return Position(attrs)
            "Select" -> return Select(attrs)
        }
        return createComponentFromClassName(componentType, attrs)
    }

    companion object {

        private val sConstructorMap = HashMap<String, Constructor<out ComponentBase>>()
        private val mConstructorSignature = arrayOf<Class<*>>(ComponentAttributes::class.java)
    }
}
