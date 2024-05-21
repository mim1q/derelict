package datagen

import tada.lib.presets.Preset
import tada.lib.presets.blocksets.WoodPresets
import tada.lib.presets.common.CommonDropPresets
import tada.lib.presets.common.CommonModelPresets
import tada.lib.presets.common.CommonRecipePresets
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation
import tada.lib.resources.model.ParentedModel
import tada.lib.resources.recipe.CraftingRecipe
import tada.lib.tags.TagManager
import tada.lib.util.Id

object CustomMetalPresets {
    private fun metalSet(id: String, oxidization: String = "", waxed: Boolean = false) = Preset {
        val (ns, name) = Id(id)

        fun blockTexture(suffix: String = "", variant: String = "", folder: String = "") =
            "$ns:$folder${name}/${oxidization}$variant${name}$suffix"

        fun blockName(suffix: String = "", variant: String = "", original: Boolean = false) =
            "${if (waxed && !original) "waxed_" else ""}${oxidization}$variant${name}$suffix"

        fun namespacedBlockName(
            suffix: String = "",
            variant: String = "",
            folder: String = "",
            original: Boolean = false
        ) = "$ns:$folder${blockName(suffix, variant, original)}"

        add(
            blockName("_block"),
            ParentedModel.block("minecraft:block/cube_all").texture("all", blockTexture("_block", folder = "block/"))
        )
        add(blockName("_block"), BlockState.createSingle(namespacedBlockName("_block", "", "block/")))
        add(
            blockName("", "cut_"),
            ParentedModel.block("minecraft:block/cube_all").texture("all", blockTexture("", "cut_", folder = "block/"))
        )
        add(blockName("", "cut_"), BlockState.createSingle(namespacedBlockName("", "cut_", "block/")))
        add(
            CommonModelPresets.pillarBlock(
                namespacedBlockName("_pillar"),
                blockTexture("_pillar_top", ""),
                blockTexture("_pillar", "")
            )
        )
        add(CommonModelPresets.stairsBlock(namespacedBlockName("", "cut_"), blockTexture("", "cut_")))
        add(
            CommonModelPresets.slabBlock(
                namespacedBlockName("", "cut_"),
                namespacedBlockName("", "cut_"),
                blockTexture("", "cut_")
            )
        )
        add(
            blockName("_grate"),
            ParentedModel.block("minecraft:block/cube_all").texture("all", blockTexture("_grate", folder = "block/"))
        )
        add(blockName("_grate"), BlockState.createSingle(namespacedBlockName("_grate", folder = "block/")))
        add(
            blockName("_chain"), ParentedModel.block("minecraft:block/chain")
                .texture("all", blockTexture("_chain", folder = "block/"))
                .texture("particle", blockTexture("_chain", folder = "block/"))
        )
        add(blockName("_chain"), BlockState.create {
            variant("axis=y", BlockStateModel(namespacedBlockName("_chain", "", "block/")))
            variant("axis=z", BlockStateModel(namespacedBlockName("_chain", "", "block/"), xRot = Rotation.CW_90))
            variant(
                "axis=x",
                BlockStateModel(
                    namespacedBlockName("_chain", "", "block/"),
                    xRot = Rotation.CW_90,
                    yRot = Rotation.CW_90
                )
            )
        })
        add(blockName("_beam"), ParentedModel.block("derelict:block/metal_beam") {
            texture("side", blockTexture("_beam", folder = "block/"))
            texture("top", blockTexture("_beam_top", folder = "block/"))
        })
        add(blockName("_beam"), BlockState.create {
            variant("axis=z", BlockStateModel(namespacedBlockName("_beam", "", "block/"), xRot = Rotation.CW_90))
            variant(
                "axis=x",
                BlockStateModel(
                    namespacedBlockName("_beam", "", "block/"),
                    xRot = Rotation.CW_90,
                    yRot = Rotation.CW_90
                )
            )
            variant("axis=y_x", BlockStateModel(namespacedBlockName("_beam", "", "block/"), yRot = Rotation.CW_90))
            variant("axis=y_z", BlockStateModel(namespacedBlockName("_beam", "", "block/")))
        })
        add(blockName("_beam"), ParentedModel.item(namespacedBlockName("_beam", "", "block/")))
        add(WoodPresets.trapdoor(namespacedBlockName(), blockTexture("_trapdoor", folder = "")))
        listOf(
            namespacedBlockName("_block"),
            namespacedBlockName("", "cut_"),
            namespacedBlockName("_grate")
        ).forEach {
            add(CommonModelPresets.itemBlockModel(it))
        }
        add(
            blockName("_chain"),
            ParentedModel.item("minecraft:item/generated").texture("layer0", blockTexture("_chain", folder = "item/"))
        )
        add(
            blockName("_ladder"),
            ParentedModel.block("derelict:block/metal_ladder").texture("0", blockTexture("_ladder", folder = "block/"))
        )
        add(blockName("_ladder"), BlockState.create {
            variant("facing=north", BlockStateModel(namespacedBlockName("_ladder", "", "block/"), yRot = Rotation.NONE))
            variant("facing=east", BlockStateModel(namespacedBlockName("_ladder", "", "block/"), yRot = Rotation.CW_90))
            variant(
                "facing=south",
                BlockStateModel(namespacedBlockName("_ladder", "", "block/"), yRot = Rotation.CW_180)
            )
            variant(
                "facing=west",
                BlockStateModel(namespacedBlockName("_ladder", "", "block/"), yRot = Rotation.CW_270)
            )
        })
        add(
            blockName("_ladder"),
            ParentedModel.item("minecraft:item/generated").texture("layer0", blockTexture("_ladder", folder = "block/"))
        )

        for (i in 1..3) {
            add(
                blockName("_patch/$i"),
                ParentedModel.block("derelict:block/metal_patch/$i")
                    .texture("0", blockTexture("_patch", folder = "block/"))
            )
        }

        add(
            rotatableMetalSheet(
                "$ns:$name",
                blockTexture("_block", folder = "block/"),
                oxidization,
                "patch",
                count = 4,
                waxed = waxed
            )
        )
        add(
            rotatableMetalSheet(
                "$ns:$name",
                blockTexture("_block", folder = "block/"),
                oxidization,
                "sheet",
                count = 8,
                waxed = waxed
            )
        )

        add(chainLinkFence(id, oxidization, waxed))

        add(
            blockName("_barbed_wire"),
            ParentedModel.block("derelict:block/barbed_wire")
                .texture("0", blockTexture("_barbed_wire", folder = "block/"))
        )
        add(blockName("_barbed_wire"), BlockState.create {
            variant("axis=x", BlockStateModel(namespacedBlockName("_barbed_wire", "", "block/")))
            variant("axis=z", BlockStateModel(namespacedBlockName("_barbed_wire", "", "block/"), yRot = Rotation.CW_90))
        })
        add(
            blockName("_barbed_wire"),
            ParentedModel.item("minecraft:item/generated")
                .texture("layer0", blockTexture("_barbed_wire", folder = "item/"))
        )

        // Recipes
        add(CommonRecipePresets.slab(namespacedBlockName("", "cut_"), namespacedBlockName("_slab", "cut_")))
        add(CommonRecipePresets.stairs(namespacedBlockName("", "cut_"), namespacedBlockName("_stairs", "cut_")))
        add(CommonRecipePresets.packed2x2(namespacedBlockName("_block"), namespacedBlockName("", "cut_")))
        add(blockName("_pillar"), CraftingRecipe.shaped(namespacedBlockName("_pillar"), 2) {
            pattern("S")
            pattern("S")
            key("S", namespacedBlockName("_slab", "cut_"))
        })
        add(blockName("_sheet"), CraftingRecipe.shaped(namespacedBlockName("_sheet"), 2) {
            pattern("BB")
            key("B", namespacedBlockName("_block"))
        })
        add(CommonRecipePresets.oneToOne(namespacedBlockName("_sheet"), namespacedBlockName("_patch"), 2))
        add(blockName("_chain"), CraftingRecipe.shaped(namespacedBlockName("_chain"), 2) {
            pattern("P")
            pattern("S")
            pattern("P")
            key("P", namespacedBlockName("_patch"))
            key("S", namespacedBlockName("_sheet"))
        })
        add(blockName("_chain_link_fence"), CraftingRecipe.shaped(namespacedBlockName("_chain_link_fence"), 6) {
            pattern("CCC")
            pattern("CCC")
            key("C", namespacedBlockName("_chain"))
        })
        add(blockName("_barbed_wire"), CraftingRecipe.shaped(namespacedBlockName("_barbed_wire"), 3) {
            pattern("CCC")
            pattern("FFF")
            key("C", namespacedBlockName("_chain"))
            key("F", namespacedBlockName("_chain_link_fence"))
        })
        add(blockName("_ladder"), CraftingRecipe.shaped(namespacedBlockName("_ladder"), 6) {
            pattern("S S")
            pattern("SSS")
            pattern("S S")
            key("S", namespacedBlockName("_sheet"))
        })
        add(blockName("_beam"), CraftingRecipe.shaped(namespacedBlockName("_beam"), 6) {
            pattern("S S")
            pattern("SBS")
            pattern("S S")
            key("B", namespacedBlockName("_block"))
            key("S", namespacedBlockName("_sheet"))
        })
        add(blockName("_grate"), CraftingRecipe.shaped(namespacedBlockName("_grate"), 5) {
            pattern("S S")
            pattern(" S ")
            pattern("S S")
            key("S", namespacedBlockName("_sheet"))
        })
        add(blockName("_trapdoor"), CraftingRecipe.shaped(namespacedBlockName("_trapdoor"), 2) {
            pattern("SSS")
            pattern("SSS")
            key("S", namespacedBlockName("_block"))
        })
        TagManager.add("minecraft:blocks/climbable", namespacedBlockName("_ladder"))
        TagManager.add("minecraft:blocks/trapdoors", namespacedBlockName("_trapdoor"))
        TagManager.add("minecraft:items/trapdoors", namespacedBlockName("_trapdoor"))
        // Item Tags
        val all = arrayOf(
            namespacedBlockName("_block"), namespacedBlockName("", "cut_"), namespacedBlockName("_pillar"),
            namespacedBlockName("_stairs", "cut_"), namespacedBlockName("_slab", "cut_"), namespacedBlockName("_chain"),
            namespacedBlockName("_grate"), namespacedBlockName("_beam"), namespacedBlockName("_ladder"),
            namespacedBlockName("_patch"), namespacedBlockName("_sheet"), namespacedBlockName("_chain_link_fence"),
            namespacedBlockName("_barbed_wire")
        )
        TagManager.add("derelict:items/${if (waxed) "waxed" else "unwaxed"}_metals", *all)

        if (waxed) {
            TagManager.add("derelict:items/waxed", namespacedBlockName("_block"), *all)
            all.forEach {
                add("${it.replace(ns, "").replace(":", "")}_from_unwaxed", CraftingRecipe.shapeless(it, 1) {
                    ingredient(it.replace("waxed_", ""))
                    ingredient("minecraft:honeycomb")
                })
            }
        }
        TagManager.add("minecraft:blocks/mineable/pickaxe", *all)
        all.forEach {
            add(CommonDropPresets.simpleDrop(it))
        }
    }

    private fun rotatableMetalSheet(
        id: String,
        particle: String,
        oxidization: String,
        type: String,
        count: Int = 8,
        waxed: Boolean = false
    ) = Preset {
        val (ns, name) = Id(id)
        val parent = "derelict:block/metal_sheets/$type"
        val prefix = (if (waxed) "waxed_" else "") + oxidization
        if (!waxed) {
            for (i in 0 until count) {
                add(
                    "metal_sheets/$oxidization${name}_${type}_$i", ParentedModel.block("${parent}_$i")
                        .texture("0", "$ns:block/$name/$oxidization${name}_$type")
                        .texture("particle", Id(particle).toString())
                )
            }
        }
        add("$prefix${name}_$type", BlockState.create {
            for (i in 0 until count) {
                variant(
                    "facing=north,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", yRot = Rotation.NONE)
                )
                variant(
                    "facing=east,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", yRot = Rotation.CW_90)
                )
                variant(
                    "facing=south,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", yRot = Rotation.CW_180)
                )
                variant(
                    "facing=west,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", yRot = Rotation.CW_270)
                )
                variant(
                    "facing=up,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", xRot = Rotation.CW_270)
                )
                variant(
                    "facing=down,rotation=$i",
                    BlockStateModel("$ns:block/metal_sheets/$oxidization${name}_${type}_$i", xRot = Rotation.CW_90)
                )
            }
        })
        add(
            "$prefix${name}_$type",
            ParentedModel.item("minecraft:item/generated").texture("layer0", "$ns:item/$name/$oxidization${name}_$type")
        )
        TagManager.add("derelict:blocks/metal_sheets", "$ns:$prefix${name}_$type")
    }

    private fun chainLinkFence(id: String, oxidization: String, waxed: Boolean) = Preset {
        val (ns, name) = Id(id)
        val prefix = (if (waxed) "waxed_" else "") + oxidization

        if (!waxed) {
            add(
                "$oxidization${name}_chain_link_fence_post",
                ParentedModel.block("derelict:block/chain_link/post")
                    .texture("0", "$ns:block/$name/$oxidization${name}_block")
            )
            add(
                "$oxidization${name}_chain_link_fence_side",
                ParentedModel.block("derelict:block/chain_link/side")
                    .texture("0", "$ns:block/$name/$oxidization${name}_chain_link_fence")
            )
        }

        fun sideModel(rotation: Rotation) =
            BlockStateModel("$ns:block/$oxidization${name}_chain_link_fence_side", yRot = rotation)

        add("$prefix${name}_chain_link_fence", BlockState.createMultipart {
            applyWhen(sideModel(Rotation.NONE), "north=true")
            applyWhen(sideModel(Rotation.CW_90), "east=true")
            applyWhen(sideModel(Rotation.CW_180), "south=true")
            applyWhen(sideModel(Rotation.CW_270), "west=true")
            applyWhenAny(
                BlockStateModel("$ns:block/$oxidization${name}_chain_link_fence_post"),
                "north=false,east=false,south=false,west=false",
                "north=true,east=false,south=false,west=false",
                "north=false,east=true,south=false,west=false",
                "north=false,east=false,south=true,west=false",
                "north=false,east=false,south=false,west=true",
                "north=true,east=true,south=false,west=false",
                "north=false,east=true,south=true,west=false",
                "north=false,east=false,south=true,west=true",
                "north=true,east=false,south=false,west=true",
                "north=true,east=true,south=true,west=false",
                "north=false,east=true,south=true,west=true",
                "north=true,east=false,south=true,west=true",
                "north=true,east=true,south=false,west=true",
                "north=true,east=true,south=true,west=true",
            )
        })

        add(
            "$prefix${name}_chain_link_fence", ParentedModel.item("minecraft:item/generated")
                .texture("layer0", "$ns:block/$name/$oxidization${name}_chain_link_fence")
        )

        TagManager.add("minecraft:blocks/climbable", "$ns:$prefix${name}_chain_link_fence")
    }

    fun threeOxidationMetalSet(id: String) = Preset {
        add(metalSet(id))
        add(metalSet(id, "weathered_"))
        add(metalSet(id, "oxidized_"))
        add(metalSet(id, waxed = true))
        add(metalSet(id, "weathered_", waxed = true))
        add(metalSet(id, "oxidized_", waxed = true))
    }
}