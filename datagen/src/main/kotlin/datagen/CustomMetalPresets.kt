package datagen

import tada.lib.presets.Preset
import tada.lib.presets.common.CommonModelPresets
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation
import tada.lib.resources.model.ParentedModel
import tada.lib.tags.TagManager
import tada.lib.util.Id

object CustomMetalPresets {
  fun metalSet(id: String, prefix: String = "", waxed: Boolean = false) = Preset {
    val (ns, name) = Id(id)

    fun blockTexture(suffix: String = "", variant: String = "", folder: String = "")
    = "$ns:$folder${name}/${prefix}$variant${name}$suffix"
    fun blockName(suffix: String = "", variant: String = "", original: Boolean = false)
    = "${if(waxed && !original) "waxed_" else ""}${prefix}$variant${name}$suffix"
    fun namespacedBlockName(suffix: String = "", variant: String = "", folder: String = "", original: Boolean = false)
    = "$ns:$folder${blockName(suffix, variant, original)}"

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
    add(blockName("_grate"), ParentedModel.block("derelict:block/metal_grate").texture("0", blockTexture("_grate", folder = "block/")))
    add(blockName("_grate"), BlockState.create {
      variant("facing=north", BlockStateModel(namespacedBlockName("_grate", "", "block/"), yRot = Rotation.NONE))
      variant("facing=east", BlockStateModel(namespacedBlockName("_grate", "", "block/"), yRot = Rotation.CW_90))
      variant("facing=south", BlockStateModel(namespacedBlockName("_grate", "", "block/"), yRot = Rotation.CW_180))
      variant("facing=west", BlockStateModel(namespacedBlockName("_grate", "", "block/"), yRot = Rotation.CW_270))
      variant("facing=up", BlockStateModel(namespacedBlockName("_grate", "", "block/"), xRot = Rotation.CW_270))
      variant("facing=down", BlockStateModel(namespacedBlockName("_grate", "", "block/"), xRot = Rotation.CW_90))
    })
    add(blockName("_chain"), ParentedModel.block("minecraft:block/chain")
      .texture("all", blockTexture("_chain", folder = "block/"))
      .texture("particle", blockTexture("_chain", folder = "block/"))
    )
    add(blockName("_chain"), BlockState.create {
      variant("axis=y", BlockStateModel(namespacedBlockName("_chain", "", "block/")))
      variant("axis=z", BlockStateModel(namespacedBlockName("_chain", "", "block/"), xRot = Rotation.CW_90))
      variant("axis=x", BlockStateModel(namespacedBlockName("_chain", "", "block/"), xRot = Rotation.CW_90, yRot = Rotation.CW_90))
    })
    listOf("vertical", "horizontal", "vertical_pile", "horizontal_pile").forEach {
      add(blockName("_beam/$it"), ParentedModel.block("derelict:block/construction_beam/$it").texture("0", blockTexture("_beam", folder = "block/")))
    }
    listOf("", "_pile").forEach {
      add(blockName("_beam$it"), BlockState.create {
        listOf(false to "horizontal", true to "vertical").forEach {pair ->
          variant("vertical=${pair.first},facing=north", BlockStateModel(namespacedBlockName("_beam/${pair.second}$it", "", "block/"), yRot = Rotation.NONE))
          variant("vertical=${pair.first},facing=east", BlockStateModel(namespacedBlockName("_beam/${pair.second}$it", "", "block/"), yRot = Rotation.CW_90))
          variant("vertical=${pair.first},facing=south", BlockStateModel(namespacedBlockName("_beam/${pair.second}$it", "", "block/"), yRot = Rotation.CW_180))
          variant("vertical=${pair.first},facing=west", BlockStateModel(namespacedBlockName("_beam/${pair.second}$it", "", "block/"), yRot = Rotation.CW_270))
        }
      })
    }
    add(blockName("_beam"), ParentedModel.item(namespacedBlockName("_beam/horizontal", "", "block/")))
    add(blockName("_beam_pile"), ParentedModel.item(namespacedBlockName("_beam/horizontal_pile", "", "block/")))
    listOf(
      namespacedBlockName("_block"),
      namespacedBlockName("", "cut_"),
      namespacedBlockName("_grate")
    ).forEach {
      add(CommonModelPresets.itemBlockModel(it))
    }
    add(blockName("_chain"), ParentedModel.item("minecraft:item/generated").texture("layer0", blockTexture("_chain", folder = "item/")))

    // Item Tags
    TagManager.add("derelict:items/${if (waxed) "waxed" else "unwaxed"}_metals",
      namespacedBlockName("_block"), namespacedBlockName("", "cut_"), namespacedBlockName("_pillar"),
      namespacedBlockName("_stairs", "cut_"), namespacedBlockName("_slab", "cut_"), namespacedBlockName("_chain"),
      namespacedBlockName("_grate"),  namespacedBlockName("_beam"), namespacedBlockName("_beam_pile")
    )
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