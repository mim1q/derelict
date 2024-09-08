package dev.mim1q.derelict.block.cobweb

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
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class SpiderSilkStrandBlock(settings: Settings) : Block(settings) {
    companion object {
        private val SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
        private val TYPE = EnumProperty.of("type", Type::class.java)
    }

    init {
        defaultState = defaultState.with(TYPE, Type.MIDDLE)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(TYPE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = getStateForPos(ctx.world, ctx.blockPos)

    private fun getStateForPos(world: BlockView, pos: BlockPos) = when {
        isFaceFullSquare(world.getBlockState(pos.up()).getOutlineShape(world, pos.up()), Direction.DOWN) ->
            defaultState.with(TYPE, Type.TOP)

        isFaceFullSquare(world.getBlockState(pos.down()).getOutlineShape(world, pos.down()), Direction.UP) ->
            defaultState.with(TYPE, Type.BOTTOM)

        else -> defaultState
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState = if (direction.axis.isVertical) getStateForPos(world, pos) else state


    @Suppress("OVERRIDE_DEPRECATION")
    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape = SHAPE

    enum class Type(name: String) : StringIdentifiable by StringWrapper(name) {
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");
    }
}