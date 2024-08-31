package datagen

import datagen.custom.AnimationPresets
import tada.lib.presets.Preset
import tada.lib.presets.common.CommonDropPresets
import tada.lib.presets.common.CommonModelPresets
import tada.lib.presets.common.CommonRecipePresets
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation
import tada.lib.resources.blockstate.BlockStateModel.Rotation.*
import tada.lib.resources.blockstate.MultipartBlockState
import tada.lib.resources.blockstate.MultipartBlockState.Entry.ConditionType
import tada.lib.resources.model.ParentedModel
import tada.lib.resources.recipe.CraftingRecipe
import tada.lib.resources.recipe.SmeltingRecipe
import tada.lib.tags.TagManager
import tada.lib.util.Id

object CustomPresets {
    fun eachWallBlock(id: String) = Preset {
        val (ns, name) = Id(id)
        add(CommonModelPresets.generatedItemModel(id))
        add(
            name, ParentedModel.block("block/vine")
                .texture("particle", "$ns:block/$name")
                .texture("vine", "$ns:block/$name")
        )
        add(name, BlockState.createMultipart {
            applyWhen(BlockStateModel("$ns:block/$name"), "north=true")
            applyWhen(BlockStateModel("$ns:block/$name"), "east=false,south=false,west=false,up=false,down=false")
            applyWhen(BlockStateModel("$ns:block/$name", yRot = CW_90, uvlock = true), "east=true")
            applyWhen(
                BlockStateModel("$ns:block/$name", yRot = CW_90, uvlock = true),
                "north=false,south=false,west=false,up=false,down=false"
            )
            applyWhen(BlockStateModel("$ns:block/$name", yRot = CW_180, uvlock = true), "south=true")
            applyWhen(
                BlockStateModel("$ns:block/$name", yRot = CW_180, uvlock = true),
                "north=false,east=false,west=false,up=false,down=false"
            )
            applyWhen(BlockStateModel("$ns:block/$name", yRot = CW_270, uvlock = true), "west=true")
            applyWhen(
                BlockStateModel("$ns:block/$name", yRot = CW_270, uvlock = true),
                "north=false,east=false,south=false,up=false,down=false"
            )
            applyWhen(BlockStateModel("$ns:block/$name", xRot = CW_270, uvlock = true), "up=true")
            applyWhen(
                BlockStateModel("$ns:block/$name", xRot = CW_270, uvlock = true),
                "north=false,east=false,south=false,west=false,down=false"
            )
            applyWhen(BlockStateModel("$ns:block/$name", xRot = CW_90, uvlock = true), "down=true")
            applyWhen(
                BlockStateModel("$ns:block/$name", xRot = CW_90, uvlock = true),
                "north=false,east=false,south=false,west=false,up=false"
            )
        })
        TagManager.add("derelict:items/general_tab", "$ns:$name")
    }

    fun smolderingEmbers() = Preset {
        val name = "smoldering_embers"
        val variantList = listOf("0", "1", "2", "3")
        fun entry(
            condition: String,
            yRot: Rotation = NONE,
            xRot: Rotation = NONE
        ): MultipartBlockState.Entry {
            val models =
                variantList.map { BlockStateModel("derelict:block/$name/$it", yRot = yRot, xRot = xRot, uvlock = true) }
            return MultipartBlockState.Entry(
                listOf(condition),
                ConditionType.WHEN,
                models[0], models[1], models[2], models[3]
            )
        }
        add(CommonModelPresets.generatedItemModel("derelict:smoldering_embers"))
        variantList.forEach {
            add(
                "$name/$it", ParentedModel.block("block/vine")
                    .texture("particle", "derelict:block/$name/$it")
                    .texture("vine", "derelict:block/$name/$it")
            )
        }
        add(name, BlockState.createMultipart {
            addEntry(entry("north=true"))
            applyWhen(BlockStateModel("block/air"), "east=false,south=false,west=false,up=false,down=false")
            addEntry(entry("east=true", yRot = CW_90))
            applyWhen(BlockStateModel("block/air"), "north=false,south=false,west=false,up=false,down=false")
            addEntry(entry("south=true", yRot = CW_180))
            applyWhen(BlockStateModel("block/air"), "north=false,east=false,west=false,up=false,down=false")
            addEntry(entry("west=true", yRot = CW_270))
            applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,up=false,down=false")
            addEntry(entry("up=true", xRot = CW_270))
            applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,west=false,down=false")
            addEntry(entry("down=true", xRot = CW_90))
            applyWhen(BlockStateModel("block/air"), "north=false,east=false,south=false,west=false,up=false")
        })
        TagManager.add("derelict:items/general_tab", "derelict:smoldering_embers")
    }

    fun smolderingLeaves() = Preset {
        val name = "smoldering_leaves"
        listOf("0", "1", "2", "3").forEach {
            add(
                "$name/$it", ParentedModel.block("block/cube_all")
                    .texture("all", "derelict:block/$name/$it")
            )
        }
        val modelList = listOf("0", "1", "2", "3").map { BlockStateModel("derelict:block/$name/$it") }
        add(name, BlockState.create {
            variant("", modelList[0], modelList[1], modelList[2], modelList[3])
        })
        add(name, ParentedModel.item("derelict:block/$name/0"))
        TagManager.add("derelict:items/general_tab", "derelict:smoldering_leaves")
    }

    fun additionalBurnedBlocksRecipes() = Preset {
        listOf(
            "unburned_planks" to "burned_planks",
            "unburned_logs" to "burned_log",
            "unburned_stripped_logs" to "stripped_burned_log",
            "unburned_wood" to "burned_wood",
            "unburned_stripped_wood" to "stripped_burned_wood",
            "unburned_wooden_stairs" to "burned_stairs",
            "unburned_wooden_slabs" to "burned_slab",
            "unburned_wooden_doors" to "burned_door",
            "unburned_wooden_trapdoors" to "burned_trapdoor",
            "unburned_wooden_fences" to "burned_fence",
            "unburned_wooden_fence_gates" to "burned_fence_gate",
            "unburned_wooden_pressure_plates" to "burned_pressure_plate",
            "unburned_wooden_buttons" to "burned_button",
            "unburned_wooden_signs" to "burned_sign",
            "unburned_leaves" to "burned_leaves",
            "unburned_cover_boards" to "burned_cover_board",
            "unburned_double_cover_boards" to "double_burned_cover_boards",
            "unburned_crossed_cover_boards" to "crossed_burned_cover_boards",
        ).forEach {
            val tag = "#derelict:${it.first}"
            val result = "derelict:${it.second}"
            add("${it.second}_from_${it.first}_blasting", SmeltingRecipe.blasting(tag, result, 1.0))
        }
        add("smoldering_embers", CraftingRecipe.shaped("derelict:smoldering_embers", 16) {
            pattern("CCC")
            pattern("CFC")
            pattern("CCC")
            key("C", "#c:coal")
            key("F", "minecraft:fire_charge")
        })
        add(CommonRecipePresets.oneToOne("derelict:smoldering_embers", "derelict:smoking_embers"))
        add("smoldering_leaves", CraftingRecipe.shapeless("derelict:smoldering_leaves", 1) {
            ingredient("derelict:burned_leaves")
            ingredient("derelict:smoldering_embers")
        })
        listOf<(String) -> String>({ "${it}grass" }, { "${it}grass_block" }, { "tall_${it}grass" }).forEach {
            add(
                "${it("dried_")}_blasting",
                SmeltingRecipe.blasting("minecraft:${it("")}", "derelict:${it("dried_")}", 1.0)
            )
            add(
                "${it("burned_")}_blasting",
                SmeltingRecipe.blasting("derelict:${it("dried_")}", "derelict:${it("burned_")}", 1.0)
            )
        }
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
                variant(
                    "facing=north,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = NONE)
                )
                variant(
                    "facing=east,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = CW_90)
                )
                variant(
                    "facing=south,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = CW_180)
                )
                variant(
                    "facing=west,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", yRot = CW_270)
                )
                variant(
                    "facing=up,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", xRot = CW_270)
                )
                variant(
                    "facing=down,rotation=$i",
                    BlockStateModel("$ns:block/cover_boards/${type}_${name}_$i", xRot = CW_90)
                )
            }
        })
        add("${prefix}${name}_cover_board$suffix", ParentedModel.item("$ns:block/cover_boards/${type}_${name}_0"))

        TagManager.add("derelict:blocks/cover_boards", "$ns:$prefix${name}_cover_board$suffix")
        add(CommonDropPresets.simpleDrop("$ns:${prefix}${name}_cover_board$suffix"))
    }

    fun coverBoards(id: String, planks: String) = Preset {
        val (ns, name) = Id(id)
        val (pNs, pName) = Id(planks)
        val particle = "$pNs:block/$pName"
        add(rotatableCoverBoard(id, particle, "single"))
        add(rotatableCoverBoard(id, particle, "double"))
        add(rotatableCoverBoard(id, particle, "crossed", 3))

        add("${name}_cover_board", CraftingRecipe.shaped("$ns:${name}_cover_board", 2) {
            pattern("#")
            pattern("#")
            pattern("#")
            key("#", planks)
        })
        add("double_${name}_cover_boards", CraftingRecipe.shaped("${ns}:double_${name}_cover_boards", 1) {
            pattern("#")
            pattern("#")
            key("#", "$ns:${name}_cover_board")
        })
        add("crossed_${name}_cover_boards", CraftingRecipe.shaped("${ns}:crossed_${name}_cover_boards", 1) {
            pattern(" #")
            pattern("# ")
            key("#", "$ns:${name}_cover_board")
        })
        add("crossed_${name}_cover_boards_flipped", CraftingRecipe.shaped("${ns}:crossed_${name}_cover_boards", 1) {
            pattern("# ")
            pattern(" #")
            key("#", "$ns:${name}_cover_board")
        })
        add(CommonRecipePresets.oneToOne("$ns:double_${name}_cover_boards", "$ns:${name}_cover_board", 2))
        add(CommonRecipePresets.oneToOne("$ns:crossed_${name}_cover_boards", "$ns:${name}_cover_board", 2))

        TagManager.add(
            "derelict:items/general_tab",
            "$ns:${name}_cover_board", "$ns:double_${name}_cover_boards", "$ns:crossed_${name}_cover_boards"
        )
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
        TagManager.add(
            "derelict:items/general_tab", "$ns:${name}_grass", "$ns:tall_${name}_grass", "$ns:${name}_grass_block",
        )
    }

    private fun textureVariantModels(id: String, parent: String, key: String, count: Int, textureFolder: String = id) =
        Preset {
            val (ns, name) = Id(id)
            val (_, tName) = Id(textureFolder)
            for (i in 0 until count) {
                add("${name}/$i", ParentedModel.block(parent).texture(key, "$ns:block/$tName/$i"))
            }
        }

    private fun flickerVariants(
        id: String,
        parent: String,
        key: String,
        count: Int,
        seed: Long,
        textureFolder: String = id
    ) = Preset {
        val (ns, name) = Id(id)
        add(textureVariantModels(id, parent, key, count, textureFolder))
        for (i in 0 until count) {
            add(AnimationPresets.flicker("$ns:block/$name/$i", seed + i))
        }
    }

    fun flickeringCubeAll(
        id: String,
        count: Int,
        seed: Long,
        onTexture: String,
        halfOnTexture: String,
        offTexture: String
    ) = Preset {
        val (ns, name) = Id(id)
        add(flickerVariants(id, "block/cube_all", "all", count, seed))
        val first = "$ns:block/$name/0"
        val rest = IntRange(1, count - 1).map { BlockStateModel("$ns:block/$name/$it") }.toTypedArray()
        add(name, ParentedModel.item(first))
        add("$name/on", ParentedModel.block("block/cube_all").texture("all", onTexture))
        add("$name/half_on", ParentedModel.block("block/cube_all").texture("all", halfOnTexture))
        add("$name/off", ParentedModel.block("block/cube_all").texture("all", offTexture))
        add(name, BlockState.create {
            variant("light_state=flickering", BlockStateModel(first), *rest)
            variant("light_state=on", BlockStateModel("$ns:block/$name/on"))
            variant("light_state=half_on", BlockStateModel("$ns:block/$name/half_on"))
            variant("light_state=off", BlockStateModel("$ns:block/$name/off"))
        })
        AnimationPresets.createIndexedBlockTextureCopies(id, count)
        TagManager.add("minecraft:blocks/mineable/pickaxe", id)
        add(CommonDropPresets.simpleDrop(id))
        TagManager.add("derelict:items/general_tab", id)
    }

    fun flickeringJackOLantern(count: Int, seed: Long) = Preset {
        val first = "derelict:block/flickering_jack_o_lantern/0"
        add(flickerVariants("derelict:flickering_jack_o_lantern", "block/jack_o_lantern", "front", count, seed))
        fun rest(rotation: Rotation) = IntRange(1, count - 1).map {
            BlockStateModel(
                "derelict:block/flickering_jack_o_lantern/$it",
                yRot = rotation
            )
        }.toTypedArray()
        add(
            "flickering_jack_o_lantern/on",
            ParentedModel.block("block/jack_o_lantern").texture("front", "minecraft:block/jack_o_lantern")
        )
        add(
            "flickering_jack_o_lantern/half_on",
            ParentedModel.block("block/jack_o_lantern").texture("front", "derelict:block/jack_o_lantern_half_on")
        )
        add(
            "flickering_jack_o_lantern/off",
            ParentedModel.block("block/jack_o_lantern").texture("front", "minecraft:block/carved_pumpkin")
        )
        add("flickering_jack_o_lantern", ParentedModel.item(first))
        add("flickering_jack_o_lantern", BlockState.create {
            variant(
                "light_state=flickering,facing=north",
                BlockStateModel(first, yRot = NONE),
                *rest(NONE)
            )
            variant(
                "light_state=flickering,facing=east",
                BlockStateModel(first, yRot = CW_90),
                *rest(CW_90)
            )
            variant(
                "light_state=flickering,facing=south",
                BlockStateModel(first, yRot = CW_180),
                *rest(CW_180)
            )
            variant(
                "light_state=flickering,facing=west",
                BlockStateModel(first, yRot = CW_270),
                *rest(CW_270)
            )
            listOf("on", "half_on", "off").forEach {
                variant(
                    "light_state=$it,facing=north",
                    BlockStateModel("derelict:block/flickering_jack_o_lantern/$it", yRot = NONE)
                )
                variant(
                    "light_state=$it,facing=east",
                    BlockStateModel("derelict:block/flickering_jack_o_lantern/$it", yRot = CW_90)
                )
                variant(
                    "light_state=$it,facing=south",
                    BlockStateModel("derelict:block/flickering_jack_o_lantern/$it", yRot = CW_180)
                )
                variant(
                    "light_state=$it,facing=west",
                    BlockStateModel("derelict:block/flickering_jack_o_lantern/$it", yRot = CW_270)
                )
            }
        })
        AnimationPresets.createIndexedBlockTextureCopies("derelict:flickering_jack_o_lantern", count)
        TagManager.add("minecraft:blocks/mineable/axe", "derelict:flickering_jack_o_lantern")
        TagManager.add("derelict:items/general_tab", "derelict:flickering_jack_o_lantern")
        add(CommonDropPresets.simpleDrop("derelict:flickering_jack_o_lantern"))
    }

    fun flickeringEndRod(
        id: String,
        count: Int,
        seed: Long,
        onTexture: String,
        halfOnTexture: String,
        offTexture: String
    ) = Preset {
        val (ns, name) = Id(id)
        fun rest(xRot: Rotation = NONE, yRot: Rotation = NONE) = IntRange(1, count - 1).map {
            BlockStateModel("$ns:block/$name/$it", xRot, yRot)
        }.toTypedArray()
        add(flickerVariants(id, "block/end_rod", "end_rod", count, seed))
        add("${name}/on", ParentedModel.block("block/end_rod").texture("end_rod", onTexture))
        add("${name}/half_on", ParentedModel.block("block/end_rod").texture("end_rod", halfOnTexture))
        add("${name}/off", ParentedModel.block("block/end_rod").texture("end_rod", offTexture))

        add(name, BlockState.create {
            listOf("flickering", "on", "half_on", "off").forEach {
                fun v(facing: String, xRot: Rotation = NONE, yRot: Rotation = NONE) =
                    if (it == "flickering") variant(
                        "light_state=flickering,facing=${facing}",
                        BlockStateModel("$ns:block/$name/0"),
                        *rest(xRot, yRot)
                    )
                    else variant("light_state=$it,facing=${facing}", BlockStateModel("$ns:block/$name/$it", xRot, yRot))

                v("up")
                v("down", xRot = CW_180)
                v("north", xRot = CW_90)
                v("east", xRot = CW_90, yRot = CW_90)
                v("south", xRot = CW_90, yRot = CW_180)
                v("west", xRot = CW_90, yRot = CW_270)
            }
        })

        add(name, ParentedModel.item("$ns:block/$name/0"))
        AnimationPresets.createIndexedBlockTextureCopies(id, count)
        TagManager.add("minecraft:blocks/mineable/pickaxe", id)
        add(AnimationPresets.flicker("$ns:item/$name", seed))
        add(CommonDropPresets.simpleDrop(id))
    }

    fun endRod(id: String) = Preset {
        val (ns, name) = Id(id)
        add(CommonModelPresets.itemBlockModel(id))
        add(name, ParentedModel.block("block/end_rod").texture("end_rod", "$ns:block/$name"))
        add(name, BlockState.create {
            variant("facing=up", BlockStateModel("$ns:block/$name"))
            variant("facing=down", BlockStateModel("$ns:block/$name", xRot = CW_180))
            variant("facing=north", BlockStateModel("$ns:block/$name", xRot = CW_90))
            variant("facing=east", BlockStateModel("$ns:block/$name", xRot = CW_90, yRot = CW_90))
            variant("facing=south", BlockStateModel("$ns:block/$name", xRot = CW_90, yRot = CW_180))
            variant("facing=west", BlockStateModel("$ns:block/$name", xRot = CW_90, yRot = CW_270))
        })

        TagManager.add("minecraft:blocks/mineable/pickaxe", id)
        add(CommonDropPresets.simpleDrop(id))
    }

    fun flickeringLantern(
        id: String,
        count: Int,
        seed: Long,
        onTexture: String,
        halfOnTexture: String,
        offTexture: String
    ) = Preset {
        val (ns, name) = Id(id)
        val rest = IntRange(1, count - 1).map { BlockStateModel("$ns:block/$name/$it") }.toTypedArray()
        val restHanging = IntRange(1, count - 1).map { BlockStateModel("$ns:block/${name}_hanging/$it") }.toTypedArray()
        add(flickerVariants(id, "block/template_lantern", "lantern", count, seed, id))
        add(flickerVariants("${id}_hanging", "block/template_hanging_lantern", "lantern", count, seed, id))
        add(CommonModelPresets.generatedItemModel(id))
        add("${name}_hanging/on", ParentedModel.block("block/template_hanging_lantern").texture("lantern", onTexture))
        add(
            "${name}_hanging/half_on",
            ParentedModel.block("block/template_hanging_lantern").texture("lantern", halfOnTexture)
        )
        add("${name}_hanging/off", ParentedModel.block("block/template_hanging_lantern").texture("lantern", offTexture))
        add("${name}/on", ParentedModel.block("block/template_lantern").texture("lantern", onTexture))
        add("${name}/half_on", ParentedModel.block("block/template_lantern").texture("lantern", halfOnTexture))
        add("${name}/off", ParentedModel.block("block/template_lantern").texture("lantern", offTexture))
        add(name, BlockState.create {
            variant("light_state=flickering,hanging=false", BlockStateModel("$ns:block/$name/0"), *rest)
            variant("light_state=flickering,hanging=true", BlockStateModel("$ns:block/${name}_hanging/0"), *restHanging)
            listOf("on", "half_on", "off").forEach {
                variant("light_state=$it,hanging=false", BlockStateModel("$ns:block/$name/$it"))
                variant("light_state=$it,hanging=true", BlockStateModel("$ns:block/${name}_hanging/$it"))
            }
        })
        AnimationPresets.createIndexedBlockTextureCopies(id, count)
        TagManager.add("minecraft:blocks/mineable/pickaxe", id)
        TagManager.add("derelict:items/general_tab", id)
        add(AnimationPresets.flicker("$ns:item/$name", seed))
        add(CommonDropPresets.simpleDrop(id))
    }

    fun lantern(id: String) = Preset {
        val (ns, name) = Id(id)
        add(CommonModelPresets.generatedItemModel(id))
        add(name, ParentedModel.block("block/template_lantern").texture("lantern", "$ns:block/$name"))
        add(
            "${name}_hanging",
            ParentedModel.block("block/template_hanging_lantern").texture("lantern", "$ns:block/${name}")
        )
        add(name, BlockState.create {
            variant("hanging=false", BlockStateModel("$ns:block/$name"))
            variant("hanging=true", BlockStateModel("$ns:block/${name}_hanging"))
        })
        TagManager.add("minecraft:blocks/mineable/pickaxe", id)
        TagManager.add("derelict:items/general_tab", id)
        add(CommonDropPresets.simpleDrop(id))
    }

    fun fancyCobweb(id: String) = Preset {
        val (ns, name) = Id(id)

        if (id != "derelict:fancy_cobweb") {
            listOf(
                "bottom", "corner_horizontal", "corner_rotated_bottom", "corner_rotated_top", "corner_straight_bottom",
                "corner_straight_top", "full", "side", "top"
            ).forEach {
                add("${name}/$it", ParentedModel.block("derelict:block/fancy_cobweb/$it")
                    .texture("0", "$ns:block/$name"))
            }
        }

        fun topModel(rotation: Rotation) = BlockStateModel("$ns:block/$name/top", yRot = rotation)
        fun sideModel(rotation: Rotation) = BlockStateModel("$ns:block/$name/side", yRot = rotation)
        fun bottomModel(rotation: Rotation) = BlockStateModel("$ns:block/$name/bottom", yRot = rotation)
        fun fullModel() = BlockStateModel("$ns:block/$name/full")
        add(CommonDropPresets.silkTouchOrShearsOnlyDrop(id))
        add(name, BlockState.createMultipart {
            applyWhenAll(topModel(NONE), "up=true", "north=true")
            applyWhenAll(topModel(CW_90), "up=true", "east=true")
            applyWhenAll(topModel(CW_180), "up=true", "south=true")
            applyWhenAll(topModel(CW_270), "up=true", "west=true")
            applyWhenAll(bottomModel(NONE), "down=true", "north=true")
            applyWhenAll(bottomModel(CW_90), "down=true", "east=true")
            applyWhenAll(bottomModel(CW_180), "down=true", "south=true")
            applyWhenAll(bottomModel(CW_270), "down=true", "west=true")
            applyWhenAll(sideModel(NONE), "west=true", "north=true")
            applyWhenAll(sideModel(CW_90), "north=true", "east=true")
            applyWhenAll(sideModel(CW_180), "east=true", "south=true")
            applyWhenAll(sideModel(CW_270), "south=true", "west=true")
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
        TagManager.add("derelict:items/general_tab", id)
    }

    fun fancyCornerCobweb(id: String) = Preset {
        val (ns, name) = Id(id)
        listOf("rotated_bottom", "rotated_top", "straight_bottom", "straight_top", "horizontal").forEach {
            add(
                "${name}_$it", ParentedModel.block("derelict:block/fancy_cobweb/corner_$it")
                    .texture("0", "$ns:block/$name")
            )
        }
        add(name, BlockState.create {
            listOf("top", "bottom").forEach {
                variant("rotation=0,type=$it", BlockStateModel("$ns:block/${name}_straight_$it"))
                variant("rotation=1,type=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = CW_90))
                variant("rotation=2,type=$it", BlockStateModel("$ns:block/${name}_straight_$it", yRot = CW_90))
                variant("rotation=3,type=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = CW_180))
                variant(
                    "rotation=4,type=$it",
                    BlockStateModel("$ns:block/${name}_straight_$it", yRot = CW_180)
                )
                variant("rotation=5,type=$it", BlockStateModel("$ns:block/${name}_rotated_$it", yRot = CW_270))
                variant(
                    "rotation=6,type=$it",
                    BlockStateModel("$ns:block/${name}_straight_$it", yRot = CW_270)
                )
                variant("rotation=7,type=$it", BlockStateModel("$ns:block/${name}_rotated_$it"))
            }
            variant("rotation=0,type=horizontal", BlockStateModel("$ns:block/${name}_horizontal"))
            variant(
                "rotation=1,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_90)
            )
            variant(
                "rotation=2,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_90)
            )
            variant(
                "rotation=3,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_180)
            )
            variant(
                "rotation=4,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_180)
            )
            variant(
                "rotation=5,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_270)
            )
            variant(
                "rotation=6,type=horizontal",
                BlockStateModel("$ns:block/${name}_horizontal", yRot = CW_270)
            )
            variant("rotation=7,type=horizontal", BlockStateModel("$ns:block/${name}_horizontal"))
        })
        add(CommonDropPresets.silkTouchOrShearsOnlyDrop(id))
        add(CommonModelPresets.generatedItemModel(id))
        TagManager.add("derelict:blocks/cobwebs", id)
        TagManager.add("derelict:items/general_tab", id)
    }

    fun miscRecipes() = Preset {
        add("aging_staff", CraftingRecipe.shaped("derelict:aging_staff") {
            pattern("  C")
            pattern(" /~")
            pattern("O  ")
            key("C", "minecraft:clock")
            key("/", "minecraft:stick")
            key("~", "minecraft:string")
            key("O", "minecraft:crying_obsidian")
        })

        add("waxing_staff", CraftingRecipe.shaped("derelict:waxing_staff") {
            pattern(" *W")
            pattern("*/*")
            pattern("D* ")
            key("*", "minecraft:honeycomb")
            key("W", "minecraft:honeycomb_block")
            key("/", "minecraft:stick")
            key("D", "minecraft:diamond")
        })

        add("noctisteel_block", CraftingRecipe.shaped("derelict:noctisteel_block", 16) {
            pattern("CIC")
            pattern("IOI")
            pattern("CIC")
            key("C", "#c:coal")
            key("I", "minecraft:iron_ingot")
            key("O", "minecraft:obsidian")
        })

        add("corner_cobweb", CraftingRecipe.shaped("derelict:corner_cobweb", 4) {
            pattern(" S")
            pattern("SS")
            key("S", "minecraft:string")
        })
        add("fancy_corner_cobweb", CraftingRecipe.shaped("derelict:fancy_corner_cobweb", 2) {
            pattern("SS")
            pattern("SS")
            key("S", "derelict:corner_cobweb")
        })
        add("fancy_cobweb", CraftingRecipe.shaped("derelict:fancy_cobweb", 2) {
            pattern(" C ")
            pattern("CSC")
            pattern(" C ")
            key("S", "minecraft:string")
            key("C", "derelict:corner_cobweb")
        })
        add("fancy_cobweb_with_spider_nest", CraftingRecipe.shapeless("derelict:fancy_cobweb_with_spider_nest") {
            ingredient("derelict:fancy_cobweb")
            ingredient("minecraft:spider_eye")
        })
    }
}