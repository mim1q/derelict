package dev.mim1q.derelict.entity.projectile

import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

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

        (world as? ServerWorld)?.spawnParticles(
            ParticleTypes.EXPLOSION,
            pos.x, pos.y + 1.0, pos.z,
            3, 0.8, 0.8, 0.8,
            0.01
        )

        world.getOtherEntities(this, Box.of(pos, 4.0, 4.0, 4.0)) {
            it !is SpiderlingEntity.Ally
        }.forEach {
            it.damage(damageSources.explosion(this, this.owner), 6.0f)
        }

        val spiderling = ModEntities.SPIDERLING_ALLY.create(world) ?: return
        spiderling.setPos(pos.x, pos.y + 0.5, pos.z)
        (spiderling as SpiderlingEntity.Ally).owner = owner?.uuid
        world.spawnEntity(spiderling)
        discard()
    }

    override fun asItemStack(): ItemStack = ItemStack.EMPTY
}