package dev.mim1q.derelict.block.metal

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction.Axis
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

open class BarbedWireBlock(settings: FabricBlockSettings) : Block(settings) {
  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    builder.add(AXIS)
  }

  @Suppress("OVERRIDE_DEPRECATION")
  override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape
    = if (state[AXIS] == Axis.X) SHAPE else SHAPE_ROTATED

  override fun getPlacementState(ctx: ItemPlacementContext): BlockState
    = defaultState.with(AXIS, ctx.horizontalPlayerFacing.rotateYClockwise().axis)

  @Suppress("OVERRIDE_DEPRECATION")
  override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
    entity.slowMovement(state, Vec3d(0.5, 0.5, 0.5))
    entity.damage(world.damageSources.cactus(), 1.5F)
  }

  companion object {
    val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 4.0, 16.0, 8.0, 12.0)
    val SHAPE_ROTATED: VoxelShape = createCuboidShape(4.0, 0.0, 0.0, 12.0, 8.0, 16.0)

    val AXIS: EnumProperty<Axis> = Properties.HORIZONTAL_AXIS
  }
}