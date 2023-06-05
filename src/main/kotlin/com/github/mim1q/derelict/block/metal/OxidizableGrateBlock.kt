package com.github.mim1q.derelict.block.metal

import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel

class OxidizableGrateBlock(
  private val oxidizationLevel: OxidationLevel,
  settings: Settings
) : GrateBlock(settings), Oxidizable {
  override fun getDegradationLevel() = oxidizationLevel
}