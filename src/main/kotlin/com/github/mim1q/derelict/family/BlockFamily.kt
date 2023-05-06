package com.github.mim1q.derelict.family

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks

abstract class BlockFamily(
  protected val name: String,
  protected val settings: FabricBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE)
) {
  abstract fun getBlocks(): Map<String, Block>
}