package com.github.mim1q.derelict.block.flickering

import com.github.mim1q.derelict.block.flickering.FlickeringBlock.Companion.LightState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class FlickeringSolidBlock(
  settings: Settings
) : Block(settings.ticksRandomly().luminance(FlickeringBlock::getLuminance)),
  FlickeringBlock {
  init {
    defaultState = defaultState.with(FlickeringBlock.LIGHT_STATE, LightState.FLICKERING)
  }
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    builder.add(FlickeringBlock.LIGHT_STATE)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
    scheduleFlicker(world, state, pos, random, this)
  }

  override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) {
    scheduleFlicker(world, state, pos, world.random, this)
  }
}