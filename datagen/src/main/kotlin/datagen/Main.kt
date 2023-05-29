package datagen

import datagen.custom.FileCopyManager
import tada.lib.generator.ResourceGenerator
import tada.lib.presets.BlockSets
import tada.lib.presets.CommonModelPresets
import tada.lib.tags.TagManager
import java.nio.file.Path

fun main(args: Array<String>) {
  println("Hello from datagen!")
  if (args.isEmpty()) throw IllegalArgumentException("Must provide an output directory")
  FileCopyManager.setupRoot(Path.of(args[1]), Path.of(args[0]))
  ResourceGenerator.create("derelict", Path.of(args[0])).apply {
    // Assets to generate
    add(BlockSets.basicWoodSet("derelict:burned"))
    add(CustomPresets.smolderingLeaves())
    add(CommonModelPresets.cubeAllBlock("derelict:burned_leaves"))
    add(CustomPresets.smolderingEmbers())
    add(CustomPresets.eachWallBlock("derelict:smoking_embers"))
    listOf("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "crimson", "warped").forEach {
      add(CustomPresets.coverBoards("derelict:$it", "minecraft:block/${it}_planks"))
    }
    add(CustomPresets.coverBoards("derelict:burned", "derelict:block/burned_planks"))
    listOf("dried", "burned").forEach {
      add(CustomPresets.grassSet("derelict:$it"))
    }
    // Flickering Lights
    add(CustomPresets.flickeringCubeAll("derelict:flickering_sea_lantern", 8, 10))
    add(CustomPresets.flickeringCubeAll("derelict:flickering_redstone_lamp", 8, 20))
    add(CustomPresets.flickeringJackOLantern(8, 30))
    add(CustomPresets.flickeringLantern("derelict:flickering_lantern", 8, 40))
    add(CustomPresets.flickeringLantern("derelict:flickering_soul_lantern", 8, 50))
    // Custom Tags
    TagManager.add("blocks/leaves", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.add("blocks/mineable/hoe", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.copy("blocks/leaves", "items/leaves")
  }.generate()
  FileCopyManager.copyFiles()
}