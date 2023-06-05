package datagen

import tada.lib.presets.Preset
import tada.lib.presets.common.CommonModelPresets
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.model.ParentedModel
import tada.lib.util.Id

object CustomMetalPresets {
  fun metalSet(id: String, prefix: String = "", waxed: Boolean = false) = Preset {
    val (ns, name) = Id(id)

    fun blockTexture(suffix: String = "", variant: String = "", folder: String = "")
    = "$ns:$folder${name}/${prefix}$variant${name}$suffix"
    fun blockName(suffix: String = "", variant: String = "")
    = "${if(waxed) "waxed_" else ""}${prefix}$variant${name}$suffix"
    fun namespacedBlockName(suffix: String = "", variant: String = "", folder: String = "")
    = "$ns:$folder${blockName(suffix, variant)}"

    add(blockName("_block"), ParentedModel.block("minecraft:block/cube_all").texture("all", blockTexture("_block", folder = "block/")))
    add(blockName("_block"), BlockState.createSingle(namespacedBlockName("_block", "", "block/")))
    add(blockName("", "cut_"), ParentedModel.block("minecraft:block/cube_all").texture("all", blockTexture("", "cut_", folder = "block/")))
    add(blockName("", "cut_"), BlockState.createSingle(namespacedBlockName("", "cut_", "block/")))
    add(CommonModelPresets.pillarBlock(
      namespacedBlockName("_pillar"),
      blockTexture("_pillar_top", ""),
      blockTexture("_pillar", "")
    ))
    add(CommonModelPresets.stairsBlock(namespacedBlockName("", "cut_"), blockTexture("", "cut_")))
    add(CommonModelPresets.slabBlock(
      namespacedBlockName("", "cut_"),
      namespacedBlockName("", "cut_"),
      blockTexture("", "cut_")
    ))

    listOf(namespacedBlockName("_block"), namespacedBlockName("", "cut_")).forEach {
      add(CommonModelPresets.itemBlockModel(it))
    }
  }

  fun fullMetalSet(id: String) = Preset {
    add(metalSet(id))
    add(metalSet(id, "exposed_"))
    add(metalSet(id, "weathered_"))
    add(metalSet(id, "oxidized_"))
    add(metalSet(id, waxed = true))
    add(metalSet(id, "exposed_", waxed = true))
    add(metalSet(id, "weathered_", waxed = true))
    add(metalSet(id, "oxidized_", waxed = true))
  }
}