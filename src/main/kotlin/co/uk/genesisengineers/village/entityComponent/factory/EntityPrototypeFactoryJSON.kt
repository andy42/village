package co.uk.genesisengineers.village.entityComponent.factory

import co.uk.genesisengineers.core.content.Context
import co.uk.genesisengineers.core.content.entityPrototypeFactory.EntityPrototypeFactory
import co.uk.genesisengineers.core.content.entityPrototypeFactory.json.ComponentAttributesJSON
import co.uk.genesisengineers.core.entity.Entity
import co.uk.genesisengineers.core.entity.component.ComponentBase
import org.json.JSONArray
import org.json.JSONObject

class EntityPrototypeFactoryJSON : EntityPrototypeFactory {

    private val componentFactory: ComponentFactory

    constructor(context: Context) {
        componentFactory = ComponentFactory(context)
    }

    constructor(componentFactory: ComponentFactory) {
        this.componentFactory = componentFactory
    }

    override fun loadEntities(data: String?): Boolean {
        if (data == null) return false

        val jsonArray = JSONArray(data) ?: return false

        for (i in 0 until jsonArray.length()) {
            val entityJson = jsonArray.getJSONObject(i)
            val entity = createEntity(entityJson)
            entityMap[entityJson.getString("id")] = entity
        }
        return true
    }

    private fun createEntity(entityJson: JSONObject): Entity {

        val componentJSONArray = entityJson.getJSONArray("components")

        val entity = Entity(idIndex++)
        for (i in 0 until componentJSONArray.length()) {
            val component = createComponent(componentJSONArray.getJSONObject(i)) ?: continue
            entity.addComponent(component)
        }
        return entity
    }

    private fun createComponent(componentJSON: JSONObject): ComponentBase? {
        val componentType = componentJSON.getString("type")
        return componentFactory.createComponent(componentType, ComponentAttributesJSON(componentJSON))
    }
}
