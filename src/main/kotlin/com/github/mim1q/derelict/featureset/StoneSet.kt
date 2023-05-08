package com.github.mim1q.derelict.featureset

import net.minecraft.block.*

class StoneSet(name: String) : FeatureSet(name) {
  var block = Block(settings)
  var stairs = StairsBlock(block.defaultState, settings)
  var slab = SlabBlock(settings)
  val wall = WallBlock(settings)
  val pressurePlate = PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, settings)
  val button = StoneButtonBlock(settings)

  override fun getBlocks() = mapOf(
    name to block,
    "${name}_stairs" to stairs,
    "${name}_slab" to slab,
    "${name}_wall" to wall,
    "${name}_pressure_plate" to pressurePlate,
    "${name}_button" to button
  )
}