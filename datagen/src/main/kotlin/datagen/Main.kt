package datagen

import tada.lib.generator.ResourceGenerator
import tada.lib.presets.BlockSets
import tada.lib.presets.CommonModelPresets
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
  }.generate()
}