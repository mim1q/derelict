package com.github.mim1q.derelict.family

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*

class WoodBlockFamily(name: String) : BlockFamily(name, FabricBlockSettings.copyOf(Blocks.OAK_WOOD)) {
  val planks = Block(settings)
  val log = PillarBlock(settings)
  val strippedLog = PillarBlock(settings)
  val wood = PillarBlock(settings)
  val strippedWood = PillarBlock(settings)
  val stairs = StairsBlock(planks.defaultState, settings)
  val slab = SlabBlock(settings)
  val door = DoorBlock(settings.nonOpaque())
  val trapdoor = TrapdoorBlock(settings.nonOpaque())
  val fence = FenceBlock(settings)
  val fenceGate = FenceGateBlock(settings)
  val button = WoodenButtonBlock(settings)
  val pressurePlate = PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, settings)

  override fun getBlocks() = mapOf(
    "${name}_planks" to planks,
    "${name}_log" to log,
    "stripped_${name}_log" to strippedLog,
    "${name}_wood" to wood,
    "stripped_${name}_wood" to strippedWood,
    "${name}_stairs" to stairs,
    "${name}_slab" to slab,
    "${name}_door" to door,
    "${name}_trapdoor" to trapdoor,
    "${name}_fence" to fence,
    "${name}_fence_gate" to fenceGate,
    "${name}_button" to button,
    "${name}_pressure_plate" to pressurePlate
  )
}