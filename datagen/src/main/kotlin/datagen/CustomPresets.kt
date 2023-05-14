package datagen

import tada.lib.presets.CommonModelPresets
import tada.lib.presets.Preset
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation
import tada.lib.resources.blockstate.MultipartBlockState
import tada.lib.resources.blockstate.MultipartBlockState.Entry.ConditionType
import tada.lib.resources.model.ParentedModel
import tada.lib.util.Id

object CustomPresets {
  fun eachWallBlock(id: String) = Preset {
    val (ns, name) = Id(id)
    add(CommonModelPresets.generatedItemModel(id))
    add(name, ParentedModel.block("block/vine")
      .texture("particle", "$ns:block/$name")
      .texture("vine", "$ns:block/$name")
    )
    add(name, BlockState.createMultipart {
      applyWhen(BlockStateModel("$ns:block/$name"), "north=true")
      applyWhen(BlockStateModel("$ns:block/$name"), "east=false,south=false,west=false,up=false,down=false")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_90, uvlock = true), "east=true")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_90, uvlock = true), "north=false,south=false,west=false,up=false,down=false")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_180, uvlock = true), "south=true")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_180, uvlock = true), "north=false,east=false,west=false,up=false,down=false")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_270, uvlock = true), "west=true")
      applyWhen(BlockStateModel("$ns:block/$name", yRot = Rotation.CW_270, uvlock = true), "north=false,east=false,south=false,up=false,down=false")
      applyWhen(BlockStateModel("$ns:block/$name", xRot = Rotation.CW_270, uvlock = true), "up=true")
      applyWhen(BlockStateModel("$ns:block/$name", xRot = Rotation.CW_270, uvlock = true), "north=false,east=false,south=false,west=false,down=false")
      applyWhen(BlockStateModel("$ns:block/$name", xRot = Rotation.CW_90, uvlock = true), "down=true")
      applyWhen(BlockStateModel("$ns:block/$name", xRot = Rotation.CW_90, uvlock = true), "north=false,east=false,south=false,west=false,up=false")
    })
  }

  fun smolderingEmbers() = Preset {
    val name = "smoldering_embers"
    val variantList = listOf("0", "1", "2", "3")
    fun entry(condition: String, yRot: Rotation = Rotation.NONE, xRot: Rotation = Rotation.NONE): MultipartBlockState.Entry {
      val models = variantList.map { BlockStateModel("derelict:block/$name/$it", yRot = yRot, xRot = xRot, uvlock = true) }
      return MultipartBlockState.Entry(
        listOf(condition),
        ConditionType.WHEN,
        models[0], models[1], models[2], models[3]
      )
    }
    add(CommonModelPresets.generatedItemModel("derelict:smoldering_embers"))
    variantList.forEach {
      add("$name/$it", ParentedModel.block("block/vine")
        .texture("particle", "derelict:block/$name/$it")
        .texture("vine", "derelict:block/$name/$it")
      )
    }
    add(name, BlockState.createMultipart {
      addEntry(entry("north=true"))
      applyWhen(BlockStateModel("block/air"), "east=false,south=false,west=false,up=false,down=false")
      addEntry(entry("east=true", yRot = Rotation.CW_90))
      applyWhen(BlockStateModel("block/air"), "north=false,south=false,west=false,up=false,down=false")
      addEntry(entry("south=true", yRot = Rotation.CW_180))
      applyWhen(BlockStateModel("block/air"), "north=false,east=false,west=false,up=false,down=false")
      addEntry(entry("west=true", yRot = Rotation.CW_270))
      applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,up=false,down=false")
      addEntry(entry("up=true", xRot = Rotation.CW_270))
      applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,west=false,down=false")
      addEntry(entry("down=true", xRot = Rotation.CW_90))
      applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,west=false,up=false")
    })
  }

  fun smolderingLeaves() = Preset {
    val name = "smoldering_leaves"
    listOf("0", "1", "2", "3").forEach {
      add("$name/$it", ParentedModel.block("block/cube_all")
        .texture("all", "derelict:block/$name/$it"))
    }
    val modelList = listOf("0", "1", "2", "3").map { BlockStateModel("derelict:block/$name/$it") }
    add(name, BlockState.create {
      variant("", modelList[0], modelList[1], modelList[2], modelList[3])
    })
    add(name, ParentedModel.item("derelict:block/$name/0"))
  }
}