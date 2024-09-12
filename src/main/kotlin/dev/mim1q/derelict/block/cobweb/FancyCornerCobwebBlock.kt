package dev.mim1q.derelict.block.cobweb

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper

class FancyCornerCobwebBlock(settings: Settings) : Block(settings) {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ROTATION, TYPE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        if (ctx.side.axis.isHorizontal) {
            return defaultState
                .with(ROTATION, getRotation(ctx, 45F))
                .with(TYPE, Type.HORIZONTAL)
        }
        return defaultState
            .with(ROTATION, getRotation(ctx))
            .with(TYPE, if (ctx.side == Direction.UP) Type.BOTTOM else Type.TOP)
    }

    private fun getRotation(ctx: ItemPlacementContext, offset: Float = 22.5F): Int {
        val yaw = MathHelper.floorMod(ctx.player?.headYaw?.plus(offset) ?: 0.0F, 360.0F)
        return (yaw / 45F).toInt() % 8
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.with(ROTATION, (state[ROTATION] + 4) % 8)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(ROTATION, (state[ROTATION] + rotation.ordinal * 2) % 8)
    }

    companion object {
        val ROTATION: IntProperty = IntProperty.of("rotation", 0, 7)
        val TYPE: EnumProperty<Type> = EnumProperty.of("type", Type::class.java)
    }

    enum class Type(private val id: String) : StringIdentifiable {
        TOP("top"),
        BOTTOM("bottom"),
        HORIZONTAL("horizontal");

        override fun asString() = id
    }
}