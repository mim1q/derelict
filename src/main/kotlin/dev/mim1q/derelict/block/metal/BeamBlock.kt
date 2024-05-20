package dev.mim1q.derelict.block.metal

import dev.mim1q.derelict.util.ShapeUtil.rotate
import dev.mim1q.derelict.util.StringWrapper
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Axis
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

open class BeamBlock(settings: Settings) : Block(settings) {
    companion object {
        val SHAPE_VERTICAL: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 2.0),
            createCuboidShape(5.0, 0.0, 0.0, 11.0, 16.0, 16.0),
            createCuboidShape(0.0, 0.0, 14.0, 16.0, 16.0, 16.0)
        )

        val SHAPE_HORIZONTAL: VoxelShape = VoxelShapes.union(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            createCuboidShape(5.0, 2.0, 0.0, 11.0, 14.0, 16.0),
            createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        )

        val BEAM_AXIS: EnumProperty<BeamRotation> = EnumProperty.of("axis", BeamRotation::class.java)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(BEAM_AXIS)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = state[BEAM_AXIS].shape

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = when (ctx.side.axis) {
        Axis.X -> defaultState.with(BEAM_AXIS, BeamRotation.X)
        Axis.Z -> defaultState.with(BEAM_AXIS, BeamRotation.Z)
        else -> when (ctx.horizontalPlayerFacing.axis) {
            Axis.X -> defaultState.with(BEAM_AXIS, BeamRotation.Y_X)
            else -> defaultState.with(BEAM_AXIS, BeamRotation.Y_Z)
        }
    }

    enum class BeamRotation(name: String, val shape: VoxelShape) : StringIdentifiable by StringWrapper(name) {
        X("x", SHAPE_HORIZONTAL.rotate(Direction.EAST)),
        Y_X("y_x", SHAPE_VERTICAL.rotate(Direction.EAST)),
        Y_Z("y_z", SHAPE_VERTICAL),
        Z("z", SHAPE_HORIZONTAL),
    }
}