package datagen

import datagen.custom.AnimationPresets
import tada.lib.presets.common.CommonModelPresets
import tada.lib.presets.Preset
import tada.lib.presets.common.CommonDropPresets
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
    add(CommonDropPresets.simpleDrop("$ns:${prefix}${name}_cover_board$suffix"))
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
    // Drops
    add(CommonDropPresets.silkTouchDrop("$ns:${name}_grass_block", "minecraft:dirt", "$ns:${name}_grass_block"))
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop("$ns:${name}_grass"))
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop("$ns:tall_${name}_grass"))
    TagManager.add("minecraft:blocks/dirt", "$ns:${name}_grass_block")
    TagManager.add("minecraft:blocks/mineable/shovel", "$ns:${name}_grass_block")
    TagManager.add("minecraft:blocks/replaceable_plants", "$ns:${name}_grass", "$ns:tall_${name}_grass")
  }

  private fun textureVariantModels(id: String, parent: String, key: String, count: Int, textureFolder: String = id) = Preset {
    val (ns, name) = Id(id)
    val (_, tName) = Id(textureFolder)
    for (i in 0 until count) {
      add("${name}/$i", ParentedModel.block(parent).texture(key, "$ns:block/$tName/$i"))
    }
  }

  private fun flickerVariants(id: String, parent: String, key: String, count: Int, seed: Long, textureFolder: String = id) = Preset {
    val (ns, name) = Id(id)
    add(textureVariantModels(id, parent, key, count, textureFolder))
    for (i in 0 until count) {
      add(AnimationPresets.flicker("$ns:block/$name/$i", seed + i))
    }
  }

  fun flickeringCubeAll(id: String, count: Int, seed: Long) = Preset {
    val (ns, name) = Id(id)
    add(flickerVariants(id, "block/cube_all", "all", count, seed))
    val first = "$ns:block/$name/0"
    val rest = IntRange(1, count - 1).map { BlockStateModel("$ns:block/$name/$it") }.toTypedArray()
    add(name, ParentedModel.item(first))
    add(name, BlockState.create {
      variant("", BlockStateModel(first), *rest)
    })
    AnimationPresets.createIndexedBlockTextureCopies(id, count)
    TagManager.add("minecraft:blocks/mineable/pickaxe", id)
    add(CommonDropPresets.simpleDrop(id))
  }

  fun flickeringJackOLantern(count: Int, seed: Long) = Preset {
    val first = "derelict:block/flickering_jack_o_lantern/0"
    add(flickerVariants("derelict:flickering_jack_o_lantern", "block/jack_o_lantern", "front", count, seed))
    fun rest(rotation: Rotation) = IntRange(1, count - 1).map { BlockStateModel("derelict:block/flickering_jack_o_lantern/$it", yRot = rotation) }.toTypedArray()
    add("flickering_jack_o_lantern", ParentedModel.item(first))
    add("flickering_jack_o_lantern", BlockState.create {
      variant("facing=north", BlockStateModel(first, yRot = Rotation.NONE), *rest(Rotation.NONE))
      variant("facing=east", BlockStateModel(first, yRot = Rotation.CW_90), *rest(Rotation.CW_90))
      variant("facing=south", BlockStateModel(first, yRot = Rotation.CW_180), *rest(Rotation.CW_180))
      variant("facing=west", BlockStateModel(first, yRot = Rotation.CW_270), *rest(Rotation.CW_270))
    })
    AnimationPresets.createIndexedBlockTextureCopies("derelict:flickering_jack_o_lantern", count)
    TagManager.add("minecraft:blocks/mineable/axe", "derelict:flickering_jack_o_lantern")
    add(CommonDropPresets.simpleDrop("derelict:flickering_jack_o_lantern"))
  }

  fun flickeringLantern(id: String, count: Int, seed: Long) = Preset {
    val (ns, name) = Id(id)
    val rest = IntRange(1, count - 1).map { BlockStateModel("$ns:block/$name/$it") }.toTypedArray()
    val restHanging = IntRange(1, count - 1).map { BlockStateModel("$ns:block/${name}_hanging/$it") }.toTypedArray()
    add(flickerVariants(id, "block/template_lantern", "lantern", count, seed, id))
    add(flickerVariants("${id}_hanging", "block/template_hanging_lantern", "lantern", count, seed, id))
    add(CommonModelPresets.generatedItemModel(id))
    add(name, BlockState.create {
      variant("hanging=false", BlockStateModel("$ns:block/$name/0"), *rest)
      variant("hanging=true", BlockStateModel("$ns:block/${name}_hanging/0"), *restHanging)
    })
    AnimationPresets.createIndexedBlockTextureCopies(id, count)
    TagManager.add("minecraft:blocks/mineable/pickaxe", id)
    add(AnimationPresets.flicker("$ns:item/$name", seed))
    add(CommonDropPresets.simpleDrop(id))
  }

  fun lantern(id: String) = Preset {
    val (ns, name) = Id(id)
    add(CommonModelPresets.generatedItemModel(id))
    add(name, ParentedModel.block("block/template_lantern").texture("lantern", "$ns:block/$name"))
    add("${name}_hanging", ParentedModel.block("block/template_hanging_lantern").texture("lantern", "$ns:block/${name}"))
    add(name, BlockState.create {
      variant("hanging=false", BlockStateModel("$ns:block/$name"))
      variant("hanging=true", BlockStateModel("$ns:block/${name}_hanging"))
    })
    TagManager.add("minecraft:blocks/mineable/pickaxe", id)
    add(CommonDropPresets.simpleDrop(id))
  }

  fun fancyCobweb(id: String) = Preset {
    val (_, name) = Id(id)
    fun topModel(rotation: Rotation) = BlockStateModel("derelict:block/fancy_cobweb/top", yRot = rotation)
    fun sideModel(rotation: Rotation) = BlockStateModel("derelict:block/fancy_cobweb/side", yRot = rotation)
    fun bottomModel(rotation: Rotation) = BlockStateModel("derelict:block/fancy_cobweb/bottom", yRot = rotation)
    fun fullModel() = BlockStateModel("derelict:block/fancy_cobweb/full")
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop(id))
    add(name, BlockState.createMultipart {
      applyWhenAll(topModel(Rotation.NONE), "up=true", "north=true")
      applyWhenAll(topModel(Rotation.CW_90), "up=true", "east=true")
      applyWhenAll(topModel(Rotation.CW_180), "up=true", "south=true")
      applyWhenAll(topModel(Rotation.CW_270), "up=true", "west=true")
      applyWhenAll(bottomModel(Rotation.NONE), "down=true", "north=true")
      applyWhenAll(bottomModel(Rotation.CW_90), "down=true", "east=true")
      applyWhenAll(bottomModel(Rotation.CW_180), "down=true", "south=true")
      applyWhenAll(bottomModel(Rotation.CW_270), "down=true", "west=true")
      applyWhenAll(sideModel(Rotation.NONE), "west=true", "north=true")
      applyWhenAll(sideModel(Rotation.CW_90), "north=true", "east=true")
      applyWhenAll(sideModel(Rotation.CW_180), "east=true", "south=true")
      applyWhenAll(sideModel(Rotation.CW_270), "south=true", "west=true")
      applyWhen(fullModel(), "north=false,east=false,west=false,south=false,up=false,down=false")
      applyWhen(fullModel(), "north=true,east=false,west=false,south=false,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=true,west=false,south=false,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=false,west=true,south=false,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=false,west=false,south=true,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=false,west=false,south=false,up=true,down=false")
      applyWhen(fullModel(), "north=false,east=false,west=false,south=false,up=false,down=true")
      applyWhen(fullModel(), "north=true,east=false,west=false,south=true,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=true,west=true,south=false,up=false,down=false")
      applyWhen(fullModel(), "north=false,east=false,west=false,south=false,up=true,down=true")
    })
    add(CommonModelPresets.generatedItemModel(id))
    TagManager.add("derelict:blocks/cobwebs", id)
  }

  fun fancyCornerCobweb(id: String) = Preset {
    val (ns, name) = Id(id)
    listOf("rotated_bottom", "rotated_top", "straight_bottom", "straight_top").forEach {
      add("${name}_$it", ParentedModel.block("derelict:block/fancy_cobweb/corner_$it")
        .texture("0", "$ns:block/$name")
      )
    }
    add(name, BlockState.create {
      listOf("top", "bottom").forEach {
        variant("rotation=0,half=$it", BlockStateModel("$ns:block/${name}_straight_$it"))
        variant("rotation=1,half=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = Rotation.CW_90))
        variant("rotation=2,half=$it", BlockStateModel("$ns:block/${name}_straight_$it", yRot = Rotation.CW_90))
        variant("rotation=3,half=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = Rotation.CW_180))
        variant("rotation=4,half=$it", BlockStateModel("$ns:block/${name}_straight_$it", yRot = Rotation.CW_180))
        variant("rotation=5,half=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = Rotation.CW_270))
        variant("rotation=6,half=$it", BlockStateModel("$ns:block/${name}_straight_$it", yRot = Rotation.CW_270))
        variant("rotation=7,half=$it", BlockStateModel("$ns:block/${name}_rotated_$it"))
      }
    })
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop(id))
    add(CommonModelPresets.generatedItemModel(id))
    TagManager.add("derelict:blocks/cobwebs", id)
  }
}