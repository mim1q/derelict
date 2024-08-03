package datagen

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import tada.lib.presets.Preset
import tada.lib.presets.blocksets.BlockSets
import tada.lib.presets.hardcoded.JsonResource
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.model.ParentedModel
import tada.lib.tags.TagManager

object CustomStonePresets {
    fun variedStoneSet(vararg textureWeights: Int) = Preset {
        val textureCount = textureWeights.size
        val baseSet = BlockSets.fullStoneSet("derelict:arachnite")

        val baseModels = baseSet.entries.filter { it.resource is ParentedModel }

        baseSet.entries.removeIf { it.resource is ParentedModel }
        val blockstates = baseSet.entries.filter { it.resource is BlockState }

        for (blockstate in blockstates) {
            blockstate.resource.postProcess {
                replaceIfHasChildNamed("model") {
                    JsonArray().apply {
                        for (j in 0..<textureCount) {
                            val newModel = it.deepCopy() as JsonObject
                            newModel.addProperty(
                                "model",
                                newModel.get("model").asString.replace("arachnite", "arachnite/arachnite_$j")
                            )
                            newModel.addProperty("weight", textureWeights[j])
                            add(newModel)
                        }
                    }
                }
            }
        }

        for (modelEntry in baseModels) {
            val model = (modelEntry.resource as ParentedModel)

            if (model.type == ParentedModel.Type.ITEM) {
                model.postProcess {
                    applyForAllNested(
                        { it is JsonPrimitive && it.isString },
                        { JsonPrimitive(it.asString.replace("arachnite", "arachnite/arachnite_0")) })
                }
                add(modelEntry.name, model)
            } else {
                val jsonObject = model.generate()

                for (i in 0..<textureCount) {
                    val newJson = jsonObject.deepCopy() as JsonObject
                    newJson.applyForAllNested(
                        { it is JsonPrimitive && it.isString },
                        { JsonPrimitive(it.asString.replace("arachnite", "arachnite/arachnite_$i")) })
                    add(
                        "arachnite/${modelEntry.name.replace("arachnite", "arachnite_$i")}",
                        JsonResource(newJson, "models/block/", "assets")
                    )
                }
            }
        }

        add(baseSet)

        TagManager.add("minecraft:block/base_stone_overworld", "derelict:arachnite")
    }

    private fun JsonElement.replaceIfHasChildNamed(childName: String, block: (JsonElement) -> JsonElement) {
        if (this.isJsonObject) {
            for (entry in this.asJsonObject.entrySet()) {
                if ((entry.value as? JsonObject)?.has(childName) == true) {
                    entry.setValue(block(entry.value))
                } else {
                    entry.value.replaceIfHasChildNamed(childName, block)
                }
            }

        } else if (this.isJsonArray) {
            this.asJsonArray.forEach { it.replaceIfHasChildNamed(childName, block) }
        }
    }

    private fun JsonElement.applyForAllNested(predicate: (JsonElement) -> Boolean, block: (JsonElement) -> JsonElement) {
        if (this is JsonObject) {
            for (entry in this.entrySet()) {
                if (entry.value is JsonObject) {
                    (entry.value as JsonObject).applyForAllNested(predicate, block)
                }
                if (predicate(entry.value)) {
                    entry.setValue(block(entry.value))
                }
            }
        } else if (this is JsonArray) {
            this.forEach { it.applyForAllNested(predicate, block) }
        }
    }
}