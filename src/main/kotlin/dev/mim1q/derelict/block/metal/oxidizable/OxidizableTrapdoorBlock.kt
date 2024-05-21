package dev.mim1q.derelict.block.metal.oxidizable

import net.minecraft.block.BlockSetType
import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.block.TrapdoorBlock
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizableTrapdoorBlock(
    private val level: Oxidizable.OxidationLevel,
    settings: Settings,
    type: BlockSetType
) : TrapdoorBlock(settings, type), Oxidizable {
    override fun getDegradationLevel(): Oxidizable.OxidationLevel = level

    override fun hasRandomTicks(state: BlockState): Boolean = true

    @Suppress("OVERRIDE_DEPRECATION")
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        tickDegradation(state, world, pos, random)
    }
}