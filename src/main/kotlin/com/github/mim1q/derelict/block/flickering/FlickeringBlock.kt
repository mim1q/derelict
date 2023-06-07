package com.github.mim1q.derelict.block.flickering

import com.github.mim1q.derelict.Derelict
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

interface FlickeringBlock {
  fun prepareFlicker(world: World, state: BlockState, pos: BlockPos, random: Random, block: Block) {
    if (world.isClient) return
    if (Derelict.CONFIG.fancyFlickeringLights()) {
      if (state[LIGHT_STATE] == LightState.FLICKERING) {
        scheduleFlicker(world, state, pos, random, block)
      }
    } else {
      world.setBlockState(pos, state.with(LIGHT_STATE, LightState.FLICKERING))
    }
  }

  fun scheduleFlicker(world: World, state: BlockState, pos: BlockPos, random: Random, block: Block) {
    if (world.isClient) return
    if (!Derelict.CONFIG.fancyFlickeringLights()) {
      world.setBlockState(pos, state.with(LIGHT_STATE, LightState.FLICKERING))
      return
    }
    val off = state[LIGHT_STATE] == LightState.OFF
    world.setBlockState(
      pos, state.with(LIGHT_STATE, if (off) LightState.ON else LightState.OFF)
    )
    val delay = when {
      off -> if (random.nextFloat() < 0.6F) random.nextInt(2) + 1 else random.nextInt(40) + 20
      else -> random.nextInt(4) + 2
    }
    if (!world.blockTickScheduler.isQueued(pos, block)) {
      world.createAndScheduleBlockTick(pos, block, delay)
    }
  }

  companion object {
    val LIGHT_STATE: EnumProperty<LightState> = EnumProperty.of("light_state", LightState::class.java)

    enum class LightState(private val id: String) : StringIdentifiable {
      FLICKERING("flickering"),
      OFF("off"),
      ON("on");

      override fun asString() = id
    }
  }
}