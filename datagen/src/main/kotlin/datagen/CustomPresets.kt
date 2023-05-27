package datagen

import tada.lib.presets.CommonModelPresets
import tada.lib.presets.Preset
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation
import tada.lib.resources.blockstate.MultipartBlockState
import tada.lib.resources.blockstate.MultipartBlockState.Entry.ConditionType
import tada.lib.resources.model.ParentedModel
import tada.lib.tags.TagManager
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

  private fun rotatableCoverBoard(id: String, particle: String, type: String, count: Int = 8) = Preset {
    val (ns, name) = Id(id)
    val parent = "derelict:block/cover_boards/$type"
    for (i in 0 until count) {
      add(
        "cover_boards/${type}_${name}_$i", ParentedModel.block("${parent}_$i")
          .texture("0", "$ns:block/cover_boards/$name")
          .texture("particle", Id(particle).toString())
      )
    }
    val prefix = if (type == "single") "" else "${type}_"
    val suffix = if (type == "single") "" else "s"
    add("${prefix}${name}_cover_board$suffix", BlockState.create {
      for (i in 0 until count) {
        variant("facing=north,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = Rotation.NONE))
        variant("facing=east,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = Rotation.CW_90))
        variant("facing=south,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = Rotation.CW_180))
        variant("facing=west,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = Rotation.CW_270))
        variant("facing=up,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", xRot = Rotation.CW_270))
        variant("facing=down,rotation=$i", BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", xRot = Rotation.CW_90))
      }
    })
    add("${prefix}${name}_cover_board$suffix", ParentedModel.item("$ns:block/cover_boards/${type}_${name}_0"))

    TagManager.add("derelict:blocks/cover_boards", "$ns:$prefix${name}_cover_board$suffix")
  }

  fun coverBoards(id: String, particle: String) = Preset {
    add(rotatableCoverBoard(id, particle, "single"))
    add(rotatableCoverBoard(id, particle, "double"))
    add(rotatableCoverBoard(id, particle, "crossed", 3))
  }

  fun grassSet(id: String) = Preset {
    val (ns, name) = Id(id)
    add(
      "${name}_grass_block",
      ParentedModel.block("minecraft:block/grass_block")
        .texture("overlay", "$ns:block/${name}_grass_block_side_overlay")
        .texture("top", "$ns:block/${name}_grass_block_top")
        .texture("side", "minecraft:block/dirt")
    )
    add("${name}_grass_block", BlockState.createSingle("$ns:block/${name}_grass_block"))
    add(CommonModelPresets.itemBlockModel("$ns:${name}_grass_block"))
    listOf("${name}_grass", "tall_${name}_grass_bottom", "tall_${name}_grass_top").forEach {
      add(CommonModelPresets.crossBlock("$ns:$it"))
    }
    add(CommonModelPresets.generatedItemModel("$ns:${name}_grass", "block"))
    add("tall_${name}_grass", ParentedModel.item("minecraft:item/generated") {
      texture("layer0", "$ns:block/tall_${name}_grass_top")
    })

    add("${name}_grass", BlockState.createSingle("$ns:block/${name}_grass"))
    add("tall_${name}_grass", BlockState.create {
      variant("half=lower", BlockStateModel("$ns:block/tall_${name}_grass_bottom"))
      variant("half=upper", BlockStateModel("$ns:block/tall_${name}_grass_top"))
    })
    TagManager.add("minecraft:blocks/dirt", "$ns:${name}_grass_block")
    TagManager.add("minecraft:blocks/replaceable_plants", "$ns:${name}_grass", "$ns:tall_${name}_grass")
  }
}