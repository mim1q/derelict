package com.github.mim1q.derelict.block.metal

import com.github.mim1q.derelict.util.ShapeUtil
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

open class BeamBlock(settings: Settings, private val fullBlock: Boolean = false) : Block(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(FACING, VERTICAL)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
    if (ctx.side.axis.isVertical) {
      return defaultState.with(VERTICAL, false).with(FACING, ctx.playerFacing)
    }
    return defaultState.with(VERTICAL, true).with(FACING, ctx.side)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun getOutlineShape(
    state: BlockState,
    world: BlockView,
    pos: BlockPos,
    context: ShapeContext
  ): VoxelShape {
    if (fullBlock) return VoxelShapes.fullCube()
    if (state[VERTICAL]) return ShapeUtil.rotate(state[FACING], VERTICAL_SHAPE)
    return ShapeUtil.rotate(state[FACING], SHAPE)
  }

  companion object {
    val FACING = Properties.HORIZONTAL_FACING!!
    val VERTICAL = BooleanProperty.of("vertical")!!

    val SHAPE = createCuboidShape(4.0, 0.0, 0.0, 12.0, 8.0, 16.0)!!
    val VERTICAL_SHAPE = createCuboidShape(4.0, 0.0, 8.0, 12.0, 16.0, 16.0)!!
  }
}