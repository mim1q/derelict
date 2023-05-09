package com.github.mim1q.derelict.featureset

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.util.Identifier

class WoodSet(
  name: Identifier,
  defaultItemSettings: FabricItemSettings = FabricItemSettings()
) : FeatureSet(name, defaultItemSettings, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)) {
  val planks = Block(defaultBlockSettings)
  val log = PillarBlock(defaultBlockSettings)
  val strippedLog = PillarBlock(defaultBlockSettings)
  val wood = PillarBlock(defaultBlockSettings)
  val strippedWood = PillarBlock(defaultBlockSettings)
  val stairs = StairsBlock(planks.defaultState, defaultBlockSettings)
  val slab = SlabBlock(defaultBlockSettings)
  val door = DoorBlock(defaultBlockSettings.nonOpaque())
  val trapdoor = TrapdoorBlock(defaultBlockSettings.nonOpaque())
  val fence = FenceBlock(defaultBlockSettings)
  val fenceGate = FenceGateBlock(defaultBlockSettings)
  val button = WoodenButtonBlock(defaultBlockSettings)
  val pressurePlate = PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, defaultBlockSettings)

  override fun register(): WoodSet = this.apply {
    registerBlockWithItem("${name}_planks", planks)
    registerBlockWithItem("${name}_log", log)
    registerBlockWithItem("stripped_${name}_log", strippedLog)
    registerBlockWithItem("${name}_wood", wood)
    registerBlockWithItem("stripped_${name}_wood", strippedWood)
    registerBlockWithItem("${name}_stairs", stairs)
    registerBlockWithItem("${name}_slab", slab)
    registerBlockWithItem("${name}_door", door)
    registerBlockWithItem("${name}_trapdoor", trapdoor)
    registerBlockWithItem("${name}_fence", fence)
    registerBlockWithItem("${name}_fence_gate", fenceGate)
    registerBlockWithItem("${name}_button", button)
    registerBlockWithItem("${name}_pressure_plate", pressurePlate)
  }
}