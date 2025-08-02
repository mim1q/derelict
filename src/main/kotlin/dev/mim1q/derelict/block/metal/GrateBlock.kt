package dev.mim1q.derelict.block.metal

import net.minecraft.block.AbstractBlock
import net.minecraft.block.BlockState
import net.minecraft.block.TransparentBlock
import net.minecraft.util.math.Direction

open class GrateBlock(settings: AbstractBlock.Settings) : TransparentBlock(settings) {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction) =
        stateFrom.block is GrateBlock || super.isSideInvisible(state, stateFrom, direction)
}