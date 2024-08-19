package dev.mim1q.derelict.block.cobweb

import dev.mim1q.derelict.tag.ModBlockTags
import net.minecraft.block.BlockState
import net.minecraft.block.LichenGrower
import net.minecraft.block.MultifaceGrowthBlock
import net.minecraft.block.SideShapeType
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

open class FancyCobwebBlock(
    settings: Settings,
    private val hasTooltip: Boolean = false
) : MultifaceGrowthBlock(settings) {
    @Suppress("OVERRIDE_DEPRECATION")
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return DIRECTIONS.any { canConnect(world, pos, it) }
    }

    private fun canConnect(world: WorldView, origin: BlockPos, direction: Direction): Boolean {
        val pos = origin.add(direction.vector)
        val state = world.getBlockState(pos)
        return state.isSideSolid(
            world,
            pos,
            direction.opposite,
            SideShapeType.CENTER
        ) || state.isIn(ModBlockTags.COBWEBS)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val pos = ctx.blockPos
        val world = ctx.world
        var state = defaultState
        for (direction in Direction.entries) {
            state = state.with(getProperty(direction), canConnect(world, pos, direction))
        }
        return state
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
        return state.with(getProperty(direction), canConnect(world, pos, direction))
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: BlockView?,
        tooltip: MutableList<Text>,
        options: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, options)
        val text = Text.translatable(DISABLED_TOOLTIP_KEY)
        if (text.string.isNotBlank() && hasTooltip) {
            tooltip.add(text.formatted(Formatting.DARK_RED))
        }
    }

    override fun getGrower(): LichenGrower = LichenGrower(this)

    companion object {
        private const val DISABLED_TOOLTIP_KEY = "block.derelict.fancy_cobweb.disabled_tooltip"
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        entity.slowMovement(state, Vec3d(0.75, 0.5, 0.75))
    }
}