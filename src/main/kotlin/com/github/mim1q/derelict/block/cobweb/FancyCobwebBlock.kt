package com.github.mim1q.derelict.block.cobweb

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CobwebBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class FancyCobwebBlock(settings: Settings) : CobwebBlock(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
    val pos = ctx.blockPos
    val north = !ctx.world.getBlockState(pos.north()).isAir
    val east = !ctx.world.getBlockState(pos.east()).isAir
    val south = !ctx.world.getBlockState(pos.south()).isAir
    val west = !ctx.world.getBlockState(pos.west()).isAir
    val up = !ctx.world.getBlockState(pos.up()).isAir
    val down = !ctx.world.getBlockState(pos.down()).isAir
    return defaultState.with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(UP, up).with(DOWN, down)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun getStateForNeighborUpdate(
    state: BlockState,
    direction: Direction,
    neighborState: BlockState,
    world: WorldAccess,
    pos: BlockPos,
    neighborPos: BlockPos
  ): BlockState {
    return state.with(getDirectionProperty(direction), !neighborState.isAir)
  }

  companion object {
    val NORTH: BooleanProperty = BooleanProperty.of("north")
    val EAST: BooleanProperty = BooleanProperty.of("east")
    val SOUTH: BooleanProperty = BooleanProperty.of("south")
    val WEST: BooleanProperty = BooleanProperty.of("west")
    val UP: BooleanProperty = BooleanProperty.of("up")
    val DOWN: BooleanProperty = BooleanProperty.of("down")

    fun getDirectionProperty(direction: Direction): BooleanProperty {
      return when (direction) {
        Direction.NORTH -> NORTH
        Direction.EAST -> EAST
        Direction.SOUTH -> SOUTH
        Direction.WEST -> WEST
        Direction.UP -> UP
        Direction.DOWN -> DOWN
      }
    }
  }
}