package com.github.mim1q.derelict.block.metal

import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel
import net.minecraft.block.PillarBlock

class OxidizablePillarBlock(
  private val level: OxidationLevel,
  settings: Settings
) : PillarBlock(settings), Oxidizable {
  override fun getDegradationLevel() = level
}