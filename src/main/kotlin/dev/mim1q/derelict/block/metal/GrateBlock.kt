package dev.mim1q.derelict.block.metal

import net.minecraft.block.AbstractGlassBlock
import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction

open class GrateBlock(settings: Settings) : AbstractGlassBlock(settings) {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction) =
        stateFrom.block is GrateBlock || super.isSideInvisible(state, stateFrom, direction)
}