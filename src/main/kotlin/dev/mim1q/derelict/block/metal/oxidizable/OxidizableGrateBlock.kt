package dev.mim1q.derelict.block.metal.oxidizable

import dev.mim1q.derelict.block.metal.GrateBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.block.Oxidizable.OxidationLevel
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizableGrateBlock(
    private val level: OxidationLevel,
    settings: Settings
) : GrateBlock(settings), Oxidizable {
    override fun getDegradationLevel(): OxidationLevel = level

    override fun hasRandomTicks(state: BlockState): Boolean =
        Oxidizable.getIncreasedOxidationBlock(state.block).isPresent

    @Suppress("OVERRIDE_DEPRECATION")
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) =
        tickDegradation(state, world, pos, random)
}