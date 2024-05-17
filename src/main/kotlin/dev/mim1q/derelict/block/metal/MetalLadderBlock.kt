package dev.mim1q.derelict.block.metal

import net.minecraft.block.BlockState
import net.minecraft.block.LadderBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

open class MetalLadderBlock(settings: Settings) : LadderBlock(settings) {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos) = true
}