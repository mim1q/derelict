package com.github.mim1q.derelict.block.metal

import com.github.mim1q.derelict.util.ShapeUtil
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

open class GrateBlock(settings: Settings) : Block(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(FACING)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState.with(FACING, ctx.side)

  @Suppress("OVERRIDE_DEPRECATION")
  override fun getOutlineShape(
    state: BlockState,
    world: BlockView,
    pos: BlockPos,
    context: ShapeContext
  ): VoxelShape = state.get(FACING).let {
    when (it) {
      Direction.UP -> SHAPE_BOTTOM
      Direction.DOWN -> SHAPE_TOP
      else -> ShapeUtil.rotate(it, SHAPE)
    }
  }

  companion object {
    val FACING: DirectionProperty = Properties.FACING
    val SHAPE_BOTTOM: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0)
    val SHAPE_TOP: VoxelShape = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
    val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 14.0, 16.0, 16.0, 16.0)
  }
}