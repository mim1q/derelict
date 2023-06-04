package datagen

import datagen.custom.FileCopyManager
import tada.lib.generator.ResourceGenerator
import tada.lib.lang.LanguageHelper
import tada.lib.presets.blocksets.BlockSets
import tada.lib.presets.common.CommonDropPresets
import tada.lib.presets.common.CommonModelPresets
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
    add(CustomPresets.smolderingLeaves())
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop("derelict:smoldering_leaves"))
    add(CommonModelPresets.cubeAllBlock("derelict:burned_leaves"))
    add(CommonDropPresets.silkTouchOrShearsOnlyDrop("derelict:burned_leaves"))
    add(CustomPresets.smolderingEmbers())
    add(CustomPresets.eachWallBlock("derelict:smoking_embers"))
    add(CommonDropPresets.simpleDrop("derelict:smoking_embers"))
    add(CommonDropPresets.simpleDrop("derelict:smoldering_embers"))
    listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "crimson", "warped").forEach {
      add(CustomPresets.coverBoards("derelict:$it", "minecraft:block/${it}_planks"))
    }
    add(CustomPresets.coverBoards("derelict:burned", "derelict:block/burned_planks"))
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
    add(CustomPresets.flickeringCubeAll("derelict:flickering_sea_lantern", 8, 10))
    add(CommonModelPresets.cubeAllBlock("derelict:broken_sea_lantern"))
    add(CommonDropPresets.simpleDrop("derelict:broken_sea_lantern"))
    add(CustomPresets.flickeringCubeAll("derelict:flickering_redstone_lamp", 8, 20))
    add(CustomPresets.flickeringJackOLantern(8, 30))
    add(CustomPresets.flickeringLantern("derelict:flickering_lantern", 8, 40))
    add(CustomPresets.lantern("derelict:broken_lantern"))
    add(CustomPresets.flickeringLantern("derelict:flickering_soul_lantern", 8, 50))
    // Custom Tags
    TagManager.add("derelict:blocks/cobwebs", "minecraft:cobweb")
    TagManager.add("blocks/leaves", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.add("blocks/mineable/hoe", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.add("blocks/mineable/axe", "#derelict:cover_boards")
    TagManager.copy("blocks/leaves", "items/leaves")
  }
  generator.generate()
  FileCopyManager.copyFiles()

  LanguageHelper.create(langPath, langHelperPath) {
    automaticallyGenerateBlockEntries(generator)
    generateMissingLangEntries()
  }
}