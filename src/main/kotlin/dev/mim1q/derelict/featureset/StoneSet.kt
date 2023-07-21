package dev.mim1q.derelict.featureset

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.util.Identifier

class StoneSet(
  id: Identifier,
  defaultBlockSettings: FabricBlockSettings = FabricBlockSettings.of(Material.STONE)
) : FeatureSet(id, defaultBlockSettings = defaultBlockSettings) {
  val block = Block(defaultBlockSettings)
  val stairs = StairsBlock(block.defaultState, defaultBlockSettings)
  val slab = SlabBlock(defaultBlockSettings)
  val wall = WallBlock(defaultBlockSettings)
  val pressurePlate = PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, defaultBlockSettings)
  val button = StoneButtonBlock(defaultBlockSettings)

  override fun register(): StoneSet = this.apply {
    registerBlockWithItem(name, block)
    registerBlockWithItem("${name}_stairs", stairs)
    registerBlockWithItem("${name}_slab", slab)
    registerBlockWithItem("${name}_wall", wall)
    registerBlockWithItem("${name}_pressure_plate", pressurePlate)
    registerBlockWithItem("${name}_button", button)
  }
}