package dev.mim1q.derelict.block.flickering

import dev.mim1q.derelict.block.flickering.FlickeringBlock.Companion.LightState
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.EndRodBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class FlickeringEndRodBlock(
    settings: Settings
) : EndRodBlock(settings.ticksRandomly().luminance(FlickeringBlock::getLuminance)),
    FlickeringBlock {
    init {
        defaultState = defaultState.with(FlickeringBlock.LIGHT_STATE, LightState.FLICKERING)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FlickeringBlock.LIGHT_STATE)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) = scheduleFlicker(world, state, pos, random, this)
    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, itemStack: ItemStack) = scheduleFlicker(world, state, pos, world.random, this)
}

class BrokenEndRodBlock(settings: Settings) : EndRodBlock(settings) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) = Unit
}