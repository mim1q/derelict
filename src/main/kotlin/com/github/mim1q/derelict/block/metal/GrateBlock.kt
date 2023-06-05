package com.github.mim1q.derelict.block.metal

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties

open class GrateBlock(settings: Settings) : Block(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(FACING)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState.with(FACING, ctx.playerFacing.opposite)

  companion object {
    val FACING: DirectionProperty = Properties.FACING
  }
}