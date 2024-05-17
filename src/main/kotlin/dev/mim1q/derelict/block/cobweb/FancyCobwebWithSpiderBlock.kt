package dev.mim1q.derelict.block.cobweb

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FancyCobwebWithSpiderBlock(
    settings: Settings,
    private val shy: Boolean = false
) : FancyCobwebBlock(settings, true), BlockEntityProvider {
    init {
        defaultState = defaultState.with(LOWERING, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LOWERING)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState) = FancyCobwebWithSpiderBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(
        entityWorld: World,
        blockState: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker<T> { world, pos, state, entity ->
            if (entity is FancyCobwebWithSpiderBlockEntity) entity.tick(world, pos, state, shy)
        }
    }

    companion object {
        val LOWERING: BooleanProperty = BooleanProperty.of("lowering")
    }
}