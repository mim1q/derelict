package com.github.mim1q.derelict.block.cobweb

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.CobwebBlock
import net.minecraft.block.enums.BlockHalf
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper

class FancyCornerCobwebBlock(settings: Settings) : CobwebBlock(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(ROTATION, HALF)
  }

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
    if (ctx.side.axis.isHorizontal) {
      val half = if (MathHelper.fractionalPart(ctx.hitPos.y) < 0.5) BlockHalf.BOTTOM else BlockHalf.TOP
      return defaultState
        .with(ROTATION, getRotation(ctx))
        .with(HALF, half)
    }
    return defaultState
      .with(ROTATION, getRotation(ctx))
      .with(HALF, if (ctx.side == Direction.UP) BlockHalf.BOTTOM else BlockHalf.TOP)
  }

  private fun getRotation(ctx: ItemPlacementContext): Int {
    val yaw = MathHelper.floorMod(ctx.player?.headYaw?.plus(22.5F) ?: 0.0F, 360.0F)
    return (yaw / 45F).toInt() % 8
  }

  companion object {
    val ROTATION: IntProperty = IntProperty.of("rotation", 0, 7)
    val HALF: EnumProperty<BlockHalf> = Properties.BLOCK_HALF
  }
}