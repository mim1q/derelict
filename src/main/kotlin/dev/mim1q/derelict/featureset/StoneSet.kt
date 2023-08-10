package dev.mim1q.derelict.featureset

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.minecraft.block.*
import net.minecraft.util.Identifier

class StoneSet(
  id: Identifier,
  defaultBlockSettings: FabricBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE)
) : FeatureSet(id, defaultBlockSettings = defaultBlockSettings) {
  val blockSetType = BlockSetTypeBuilder.copyOf(BlockSetType.STONE).build(id)

  val block = Block(defaultBlockSettings)
  val stairs = StairsBlock(block.defaultState, defaultBlockSettings)
  val slab = SlabBlock(defaultBlockSettings)
  val wall = WallBlock(defaultBlockSettings)
  val pressurePlate = PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, defaultBlockSettings, blockSetType)
  val button = ButtonBlock(defaultBlockSettings, blockSetType, 5, false)

  override fun register(): StoneSet = this.apply {
    registerBlockWithItem(name, block)
    registerBlockWithItem("${name}_stairs", stairs)
    registerBlockWithItem("${name}_slab", slab)
    registerBlockWithItem("${name}_wall", wall)
    registerBlockWithItem("${name}_pressure_plate", pressurePlate)
    registerBlockWithItem("${name}_button", button)
  }
}