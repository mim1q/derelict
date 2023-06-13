package com.github.mim1q.derelict.block.metal.oxidizable

import com.github.mim1q.derelict.block.metal.MetalLadderBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizableMetalLadderBlock(
  private val oxidizationLevel: Oxidizable.OxidationLevel,
  settings: Settings
) : MetalLadderBlock(settings), Oxidizable {
  override fun getDegradationLevel() = oxidizationLevel

  @Suppress("OVERRIDE_DEPRECATION")
  override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
    tickDegradation(state, world, pos, random)
  }

  override fun hasRandomTicks(state: BlockState): Boolean {
    return Oxidizable.getIncreasedOxidationBlock(state.block).isPresent
  }
}