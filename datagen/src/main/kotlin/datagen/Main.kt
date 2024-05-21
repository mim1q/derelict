package datagen

import datagen.custom.CustomTagPresets
import datagen.custom.FileCopyManager
import datagen.custom.ImageAtlases
import tada.lib.generator.ResourceGenerator
import tada.lib.lang.LanguageHelper
import tada.lib.presets.blocksets.BlockSets
import tada.lib.presets.common.CommonDropPresets
import tada.lib.presets.common.CommonModelPresets
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
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb"))
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb_with_spider_nest"))
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb_with_spider"))
        add(CustomPresets.fancyCobweb("derelict:fancy_cobweb_with_shy_spider"))
        add(CustomPresets.fancyCornerCobweb("derelict:fancy_corner_cobweb"))
        add(CustomPresets.fancyCornerCobweb("derelict:corner_cobweb"))
        // Flickering Lights
        add(
            CustomPresets.flickeringCubeAll(
                "derelict:flickering_sea_lantern", 8, 10, "minecraft:block/sea_lantern",
                "derelict:block/sea_lantern_half_on", "derelict:block/broken_sea_lantern"
            )
        )
        add(CommonModelPresets.cubeAllBlock("derelict:broken_sea_lantern"))
        add(CommonDropPresets.simpleDrop("derelict:broken_sea_lantern"))
        add(
            CustomPresets.flickeringCubeAll(
                "derelict:flickering_redstone_lamp", 8, 20, "minecraft:block/redstone_lamp_on",
                "derelict:block/redstone_lamp_half_on", "minecraft:block/redstone_lamp"
            )
        )
        add(CustomPresets.flickeringJackOLantern(8, 30))
        add(
            CustomPresets.flickeringLantern(
                "derelict:flickering_lantern", 8, 40,
                "minecraft:block/lantern", "derelict:block/lantern_half_on", "derelict:block/broken_lantern"
            )
        )
        add(CustomPresets.lantern("derelict:broken_lantern"))
        add(
            CustomPresets.flickeringLantern(
                "derelict:flickering_soul_lantern", 8, 50,
                "minecraft:block/soul_lantern", "derelict:block/soul_lantern_half_on", "derelict:block/broken_lantern"
            )
        )
        // Metal
        add(CustomMetalPresets.threeOxidationMetalSet("derelict:noctisteel"))
        // Handheld items
        listOf("aging_staff", "waxing_staff").forEach {
            add(it, ParentedModel.item("minecraft:item/handheld").texture("layer0", "derelict:item/$it"))
        }
        // Custom Tags
        TagManager.add("derelict:blocks/cobwebs", "minecraft:cobweb")
        TagManager.add("blocks/leaves", "derelict:burned_leaves", "derelict:smoldering_leaves")
        TagManager.add("blocks/mineable/hoe", "derelict:burned_leaves", "derelict:smoldering_leaves")
        TagManager.add("blocks/mineable/axe", "#derelict:cover_boards")
        TagManager.add(
            "blocks/mineable/pickaxe",
            "derelict:broken_lantern", "derelict:broken_sea_lantern"
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
        TagManager.add("derelict:entity_types/spawns_spiderlings_on_death",
            "minecraft:spider",
            "minecraft:cave_spider",
        )
    }
    generator.generate()
    FileCopyManager.copyFiles()

    LanguageHelper.create(langPath, langHelperPath) {
        automaticallyGenerateBlockEntries(generator)
        generateMissingLangEntries()
    }

    ImageAtlases.getMetalAtlas(resourcePath.resolve("atlas"), generatedPath, "noctisteel").save()
}