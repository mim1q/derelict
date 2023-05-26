package com.github.mim1q.derelict.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

sealed class CoverBoardBlock(
  settings: Settings
) : Block(settings) {
  protected abstract fun getRotationProperty(): IntProperty

  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(ROTATION_8, FACING, LOCKED)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
    return defaultState.with(FACING, ctx.side).with(LOCKED, false)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun onUse(
    state: BlockState,
    world: World,
    pos: BlockPos,
    player: PlayerEntity,
    hand: Hand,
    hit: BlockHitResult
  ): ActionResult {
    if (player.isSneaking) {
      world.setBlockState(pos, state.cycle(LOCKED))
      return ActionResult.SUCCESS
    }
    if (state.get(LOCKED)) return ActionResult.PASS
    world.setBlockState(pos, state.cycle(getRotationProperty()))
    return ActionResult.SUCCESS
  }

  class Normal(settings: Settings) : CoverBoardBlock(settings) {
    override fun getRotationProperty(): IntProperty = ROTATION_8
  }

  class Crossed(settings: Settings) : CoverBoardBlock(settings) {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
      builder.add(ROTATION_3, FACING, LOCKED)
    }

    override fun getRotationProperty(): IntProperty = ROTATION_3
  }

  companion object {
    val FACING: DirectionProperty = Properties.FACING
    val ROTATION_8: IntProperty = IntProperty.of("rotation", 0, 7)
    val ROTATION_3: IntProperty = IntProperty.of("rotation", 0, 2)
    val LOCKED: BooleanProperty = BooleanProperty.of("locked")
  }
}