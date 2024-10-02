package dev.mim1q.derelict.entity.projectile

import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior

class SpiderEggProjectile(
    type: EntityType<out PersistentProjectileEntity>,
    world: World
) : PersistentProjectileEntity(type, world) {
    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        explodeIntoSpiderling(entityHitResult.pos)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        explodeIntoSpiderling(blockHitResult.pos)
    }

    private fun explodeIntoSpiderling(pos: Vec3d) {
        if (world.isClient) return

        world.createExplosion(
            this, damageSources.explosion(this, owner), object : ExplosionBehavior() {
                override fun canDestroyBlock(
                    explosion: Explosion,
                    world: BlockView,
                    pos: BlockPos,
                    state: BlockState,
                    power: Float
                ): Boolean = false
            },
            pos.x, pos.y, pos.z, 1f, false, World.ExplosionSourceType.NONE
        )

        val spiderling = ModEntities.SPIDERLING_ALLY.create(world) ?: return
        spiderling.setPos(pos.x, pos.y + 0.5, pos.z)
        (spiderling as SpiderlingEntity.Ally).owner = owner?.uuid
        world.spawnEntity(spiderling)
        discard()
    }

    override fun asItemStack(): ItemStack = ItemStack.EMPTY
}