package datagen

import tada.lib.generator.ResourceGenerator
import tada.lib.presets.BlockSets
import tada.lib.presets.CommonModelPresets
import tada.lib.tags.TagManager
import java.nio.file.Path

fun main(args: Array<String>) {
  println("Hello from datagen!")
  if (args.isEmpty()) throw IllegalArgumentException("Must provide an output directory")
  ResourceGenerator.create("derelict", Path.of(args[0])).apply {
    // Assets to generate
    add(BlockSets.basicWoodSet("derelict:burned"))
    add(CustomPresets.smolderingLeaves())
    add(CommonModelPresets.cubeAllBlock("derelict:burned_leaves"))
    add(CustomPresets.smolderingEmbers())
    add(CustomPresets.eachWallBlock("derelict:smoking_embers"))
    add(CustomPresets.coverBoards("derelict:oak", "minecraft:block/oak_planks"))
    // Custom Tags
    TagManager.add("blocks/leaves", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.add("blocks/mineable/hoe", "derelict:burned_leaves", "derelict:smoldering_leaves")
    TagManager.copy("blocks/leaves", "items/leaves")
  }.generate()
}