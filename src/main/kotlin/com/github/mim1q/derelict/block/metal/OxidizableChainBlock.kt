package com.github.mim1q.derelict.block.metal

import net.minecraft.block.ChainBlock
import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel

class OxidizableChainBlock(
  private val level: OxidationLevel,
  settings: Settings
) : ChainBlock(settings), Oxidizable {
  override fun getDegradationLevel() = level
}