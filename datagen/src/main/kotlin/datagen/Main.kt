package datagen

import datagen.custom.CustomTagPresets
import datagen.custom.FileCopyManager
import datagen.custom.ImageAtlases
import tada.lib.generator.BeautifiedJsonFormatter
import tada.lib.generator.FilesystemFileSaver
import tada.lib.generator.ResourceGenerator
import tada.lib.lang.FlattenedJson
import tada.lib.lang.LanguageHelper
import tada.lib.presets.blocksets.BlockSets
import tada.lib.presets.common.CommonDropPresets
import tada.lib.presets.common.CommonModelPresets
import tada.lib.resources.blockstate.BlockState
import tada.lib.resources.blockstate.BlockStateModel
import tada.lib.resources.blockstate.BlockStateModel.Rotation.*
import tada.lib.resources.model.ParentedModel
import tada.lib.tags.TagManager
import java.nio.file.Path

fun main(args: Array<String>) {
    println("Hello from datagen!")
    if (args.size != 4) throw IllegalArgumentException("Wrong number of arguments")
    val generatedPath = Path.of(args[0])
    val resourcePath = Path.of(args[1])
    val langPath = Path.of(args[2])
    val langHelperPath = Path.of(args[3])
    FileCopyManager.setupRoot(resourcePath, generatedPath)
    val generator = ResourceGenerator.create("derelict", generatedPath).apply {
        // Assets to generate
        add(BlockSets.basicWoodSet("derelict:burned"))
        add(CustomPresets.additionalBurnedBlocksRecipes())
        add(CustomPresets.miscRecipes())
        add(CustomTagPresets.unburnedWoodTags())
        add(CustomPresets.smolderingLeaves())
        add(CommonDropPresets.silkTouchOrShearsOnlyDrop("derelict:smoldering_leaves"))
        add(CommonModelPresets.cubeAllBlock("derelict:burned_leaves"))
        add(CommonDropPresets.silkTouchOrShearsOnlyDrop("derelict:burned_leaves"))
        add(CustomPresets.smolderingEmbers())
        add(CustomPresets.eachWallBlock("derelict:smoking_embers"))
        add(CommonDropPresets.simpleDrop("derelict:smoking_embers"))
        add(CommonDropPresets.simpleDrop("derelict:smoldering_embers"))
        listOf(
            "oak",
            "spruce",
            "birch",
            "jungle",
            "acacia",
            "dark_oak",
            "mangrove",
            "crimson",
            "warped",
            "cherry",
            "bamboo"
        ).forEach {
            add(CustomPresets.coverBoards("derelict:$it", "minecraft:${it}_planks"))
        }
        add(CustomPresets.coverBoards("derelict:burned", "derelict:burned_planks"))
        listOf("dried", "burned").forEach {
            add(CustomPresets.grassSet("derelict:$it"))
        }
        // Cobwebs
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb"))
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb_with_spider_nest"))
        add(CustomPresets.eachWallBlock("derelict:wall_cobweb"))
        add(CustomPresets.fancyCornerCobweb("derelict:fancy_corner_cobweb"))
        add(CustomPresets.fancyCornerCobweb("derelict:corner_cobweb"))
        arrayOf("spider_silk_strand", "spider_silk_strand_top", "spider_silk_strand_bottom").forEach {
            add(it, ParentedModel.block("block/cross").texture("cross", "derelict:block/$it"))
        }
        add("spider_silk_strand", BlockState.create {
            variant("type=top", "derelict:block/spider_silk_strand_top")
            variant("type=middle", "derelict:block/spider_silk_strand")
            variant("type=bottom", "derelict:block/spider_silk_strand_bottom")
        })
        add("spider_silk_strand", ParentedModel.item("item/generated").texture("layer0", "derelict:block/spider_silk_strand_top"))

        add(CommonModelPresets.cubeAllBlock("derelict:spider_egg_block"))
        add(CommonDropPresets.silkTouchDrop("derelict:spider_egg_block"))

        listOf(
            "spider_egg", "spider_egg_cluster", "spider_spider_egg", "cave_spider_spider_egg", "jumping_spider_spider_egg",
            "web_caster_spider_egg", "fake_spider_egg", "spiderling_spider_egg"
        ).forEach {
            add(it, BlockState.createSingle("derelict:block/spider_egg_block"))
        }
        add(CommonModelPresets.generatedItemModel("derelict:spider_egg"))
        add(CommonDropPresets.silkTouchDrop("derelict:spider_egg"))
        add(CommonModelPresets.generatedItemModel("derelict:spider_egg_cluster"))
        add(CommonDropPresets.silkTouchDrop("derelict:spider_egg_cluster"))

        add("spider_molt", BlockState.create {
            variant("facing=north", BlockStateModel("derelict:block/spider_molt"))
            variant("facing=east", BlockStateModel("derelict:block/spider_molt", yRot = CW_90))
            variant("facing=south", BlockStateModel("derelict:block/spider_molt", yRot = CW_180))
            variant("facing=west", BlockStateModel("derelict:block/spider_molt", yRot = CW_270))
        })
        add(CommonModelPresets.itemBlockModel("derelict:spider_molt"))
        add(CommonDropPresets.silkTouchDrop("derelict:spider_molt"))

        // Flickering Lights
        add(
            CustomPresets.flickeringCubeAll(
                "derelict:flickering_sea_lantern", 8, 10, "minecraft:block/sea_lantern",
                "derelict:block/sea_lantern_half_on", "derelict:block/extinguished_sea_lantern"
            )
        )
        add(CommonModelPresets.cubeAllBlock("derelict:extinguished_sea_lantern"))
        add(CommonDropPresets.simpleDrop("derelict:extinguished_sea_lantern"))
        add(CommonModelPresets.cubeAllBlock("derelict:spider_silk"))
        add(CommonDropPresets.silkTouchOrShearsOnlyDrop("derelict:spider_silk"))
        add(
            CustomPresets.flickeringCubeAll(
                "derelict:flickering_redstone_lamp", 8, 20, "minecraft:block/redstone_lamp_on",
                "derelict:block/redstone_lamp_half_on", "minecraft:block/redstone_lamp"
            )
        )
        // Froglights
        listOf("ochre", "verdant", "pearlescent").forEach {
            add(CommonModelPresets.cubeAllBlock("derelict:extinguished_${it}_froglight"))
            add(CommonDropPresets.simpleDrop("derelict:extinguished_${it}_froglight"))
            add(
                CustomPresets.flickeringCubeAll(
                    "derelict:flickering_${it}_froglight",
                    8,
                    30,
                    "minecraft:block/${it}_froglight_side",
                    "derelict:block/${it}_froglight_half_on",
                    "derelict:block/extinguished_${it}_froglight"
                )
            )
        }
        add(CustomPresets.flickeringCubeAll("derelict:flickering_glowstone", 8, 40, "minecraft:block/glowstone", "derelict:block/glowstone_half_on", "derelict:block/extinguished_glowstone"))
        add(CommonModelPresets.cubeAllBlock("derelict:extinguished_glowstone"))
        add(CommonDropPresets.simpleDrop("derelict:extinguished_glowstone"))

        add(CustomPresets.flickeringCubeAll("derelict:flickering_shroomlight", 8, 50, "minecraft:block/shroomlight", "derelict:block/shroomlight_half_on", "derelict:block/extinguished_shroomlight"))
        add(CommonModelPresets.cubeAllBlock("derelict:extinguished_shroomlight"))
        add(CommonDropPresets.simpleDrop("derelict:extinguished_shroomlight"))

        add(CustomPresets.flickeringJackOLantern(8, 30))
        add(
            CustomPresets.flickeringLantern(
                "derelict:flickering_lantern", 8, 40,
                "minecraft:block/lantern", "derelict:block/lantern_half_on", "derelict:block/extinguished_lantern"
            )
        )
        add(CustomPresets.lantern("derelict:extinguished_lantern"))
        add(
            CustomPresets.flickeringLantern(
                "derelict:flickering_soul_lantern", 8, 50,
                "minecraft:block/soul_lantern", "derelict:block/soul_lantern_half_on", "derelict:block/extinguished_lantern"
            )
        )

        add(
            CustomPresets.flickeringEndRod(
                "derelict:flickering_end_rod", 8, 60, "minecraft:block/end_rod", "derelict:block/end_rod_half_on", "derelict:block/extinguished_end_rod"
            )
        )
        add(CustomPresets.endRod("derelict:extinguished_end_rod"))

        // Metal
        add(CustomMetalPresets.threeOxidationMetalSet("derelict:noctisteel"))
        // Stone
        add(CustomStonePresets.variedStoneSet(30, 5, 5, 5, 5, 5, 1, 1, 1, 2, 2, 2, 30, 5, 5, 5, 5, 5, 1, 1, 1, 2, 2, 2))
        // Handheld items
        listOf("aging_staff", "waxing_staff").forEach {
            add(it, ParentedModel.item("minecraft:item/handheld").texture("layer0", "derelict:item/$it"))
        }
        // Generated items
        listOf("spiderling_in_a_bucket").forEach {
            add(it, ParentedModel.item("minecraft:item/generated").texture("layer0", "derelict:item/$it"))
        }
        Constants.SPAWN_EGGS.forEach {
            add(CommonModelPresets.generatedItemModel("derelict:${it}_spawn_egg"))
        }
        // Template pools
        spiderCavesTemplatePools()

        // Custom Tags
        TagManager.add("derelict:blocks/cobwebs", "minecraft:cobweb", "derelict:spider_silk")
        TagManager.add("blocks/leaves", "derelict:burned_leaves", "derelict:smoldering_leaves")
        TagManager.add("blocks/mineable/hoe", "derelict:burned_leaves", "derelict:smoldering_leaves", "derelict:flickering_shroomlight", "derelict:extinguished_shroomlight")
        TagManager.add("blocks/mineable/axe", "#derelict:cover_boards")
        TagManager.add("blocks/mineable/pickaxe",
            "derelict:extinguished_lantern", "derelict:extinguished_sea_lantern"
        )
        TagManager.copy("blocks/leaves", "items/leaves")
        TagManager.add(
            "derelict:items/general_tab",
            "derelict:aging_staff",
            "derelict:waxing_staff",
            "derelict:burned_planks",
            "derelict:burned_stairs",
            "derelict:burned_slab",
            "derelict:burned_fence",
            "derelict:burned_fence_gate",
            "derelict:burned_door",
            "derelict:burned_button",
            "derelict:burned_pressure_plate",
            "derelict:burned_sign",
            "derelict:burned_log",
            "derelict:burned_wood",
            "derelict:stripped_burned_log",
            "derelict:stripped_burned_wood",
            "derelict:burned_leaves",
            "derelict:smoldering_leaves",
            "derelict:burned_trapdoor"
        )
        listOf("", "exposed_", "weathered_", "oxidized_").forEach {
            val suffix = if (it == "") "_block" else ""
            TagManager.add(
                "derelict:items/waxed", "minecraft:waxed_${it}copper$suffix", "minecraft:waxed_${it}cut_copper",
                "minecraft:waxed_${it}cut_copper_stairs", "minecraft:waxed_${it}cut_copper_slab"
            )
        }
        TagManager.add(
            "derelict:entity_types/spawns_spiderlings_on_death",
            "minecraft:spider",
            "minecraft:cave_spider",
        )
    }
    generator.printInfo()
    generator.generate()
    FileCopyManager.copyFiles()

    ResourceGenerator(
        "derelict",
        langPath.resolve("../../../"),
        FilesystemFileSaver,
        BeautifiedJsonFormatter
    ).apply {
        add("en_us", FlattenedJson(langHelperPath.resolve("en_us_map.json5").toFile(), "lang", "assets"))
    }.generate()

    LanguageHelper.create(langPath, langHelperPath) {
//        automaticallyGenerateBlockEntries(generator)
        generateMissingLangEntries()
    }

    ImageAtlases.getMetalAtlas(resourcePath.resolve("atlas"), generatedPath, "noctisteel").save()
    ImageAtlases.getColorsAtlas(resourcePath.resolve("atlas"), generatedPath, "sock", "entity/spider/sock", 64).save()
    ImageAtlases.getEggsAtlas(resourcePath.resolve("atlas"), generatedPath).save()
    ImageAtlases.getStoneAtlas(resourcePath.resolve("atlas"), generatedPath, "arachnite", 24).save()
}