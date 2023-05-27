package com.github.mim1q.derelict.featureset

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.FernBlock
import net.minecraft.block.TallPlantBlock
import net.minecraft.util.Identifier

class GrassSet(
  id: Identifier,
  defaultItemSettings: FabricItemSettings
) : FeatureSet(id, defaultItemSettings) {
  val grassBlock = registerBlockWithItem("${name}_grass_block", Block(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK)))
  val grass = registerBlockWithItem("${name}_grass", FernBlock(FabricBlockSettings.copyOf(Blocks.GRASS)))
  val tallGrass = registerBlockWithItem("tall_${name}_grass", TallPlantBlock(FabricBlockSettings.copyOf(Blocks.GRASS)))

  override fun register(): FeatureSet = this
}