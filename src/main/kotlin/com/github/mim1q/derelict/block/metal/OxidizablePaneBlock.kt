package com.github.mim1q.derelict.block.metal

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.block.PaneBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizablePaneBlock(
  private val oxidizationLevel: Oxidizable.OxidationLevel,
  settings: FabricBlockSettings
) : PaneBlock(settings), Oxidizable {
  override fun getDegradationLevel() = oxidizationLevel

  @Suppress("OVERRIDE_DEPRECATION")
  override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
    tickDegradation(state, world, pos, random)
  }

  override fun hasRandomTicks(state: BlockState): Boolean {
    return Oxidizable.getIncreasedOxidationBlock(state.block).isPresent
  }
}