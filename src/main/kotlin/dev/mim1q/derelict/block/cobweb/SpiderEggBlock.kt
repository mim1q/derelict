package dev.mim1q.derelict.block.cobweb

import dev.mim1q.derelict.init.ModBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SpiderEggBlock(
    settings: Settings,
    val big: Boolean = false,
    private val entityType: EntityType<out LivingEntity>? = null,
    private val count: (Random) -> Int = { 1 },
    private val ticks: Boolean = entityType != null
) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = SpiderEggBlockEntity(pos, state)

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? = if (!ticks) null else
        checkType(type, ModBlockEntities.SPIDER_EGG_BLOCK_ENTITY) { entityWorld, entityPos, entityState, entity ->
            entity.tick(entityWorld, entityPos, entityState)
        }

    class SpiderEggBlockEntity(pos: BlockPos, state: BlockState) :
        BlockEntity(ModBlockEntities.SPIDER_EGG_BLOCK_ENTITY, pos, state) {

        fun tick(world: World, pos: BlockPos, state: BlockState) {
            if (
                world.time % 10L == (pos.hashCode().toLong() % 10L)
                && world.getClosestPlayer(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 5.0, true) != null
            ) {
                (state.block as? SpiderEggBlock)?.let { block ->
                    val random = world.random
                    if (block.entityType != null) {
                        repeat(block.count(random)) {
                            val entity = block.entityType.create(world) ?: return@let
                            entity.setPos(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
                            world.spawnEntity(entity)
                        }
                    }
                }

                world.breakBlock(pos, false)
            }
        }
    }
}