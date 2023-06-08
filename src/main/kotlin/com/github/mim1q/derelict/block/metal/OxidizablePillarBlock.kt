package com.github.mim1q.derelict.block.metal

import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel
import net.minecraft.block.PillarBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizablePillarBlock(
  private val level: OxidationLevel,
  settings: Settings
) : PillarBlock(settings), Oxidizable {
  override fun getDegradationLevel() = level

  @Suppress("OVERRIDE_DEPRECATION")
  override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
    tickDegradation(state, world, pos, random)
  }

  override fun hasRandomTicks(state: BlockState): Boolean {
    return Oxidizable.getIncreasedOxidationBlock(state.block).isPresent
  }
}