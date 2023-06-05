package com.github.mim1q.derelict.block.metal

import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel

class OxidizableBeamBlock(
  private val oxidizationLevel: OxidationLevel,
  settings: Settings
) : BeamBlock(settings), Oxidizable {
  override fun getDegradationLevel() = oxidizationLevel
}