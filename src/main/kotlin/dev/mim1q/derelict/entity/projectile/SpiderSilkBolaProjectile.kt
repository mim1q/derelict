package dev.mim1q.derelict.entity.projectile

import dev.mim1q.derelict.init.ModStatusEffects
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class SpiderSilkBolaProjectile(
    entityType: EntityType<SpiderSilkBolaProjectile>,
    world: World
) : PersistentProjectileEntity(entityType, world) {
    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        if (!world.isClient) {
            (world as? ServerWorld)?.applyCobwebOnNearbyEntities(
                entityHitResult.entity as? LivingEntity,
                BlockPos.ofFloored(entityHitResult.pos)
            )
            discard()
        }
    }

    override fun onBlockHit(blockHitResult: BlockHitResult) {
        super.onBlockHit(blockHitResult)
        if (!world.isClient) {
            (world as? ServerWorld)?.applyCobwebOnNearbyEntities(null, blockHitResult.blockPos)
            discard()
        }
    }

    private fun ServerWorld.applyCobwebOnNearbyEntities(entity: LivingEntity?, pos: BlockPos) {
        entity?.applyCobweb(2, 60, 4f)

        getOtherEntities(this@SpiderSilkBolaProjectile.owner, Box.of(Vec3d.ofCenter(pos), 5.0, 5.0, 5.0)).forEach {
            (it as? LivingEntity)?.applyCobweb(1, 40, 2f)
        }

        spawnParticles(
            BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COBWEB.defaultState),
            this@SpiderSilkBolaProjectile.x,
            this@SpiderSilkBolaProjectile.y + 0.5,
            this@SpiderSilkBolaProjectile.z,
            25,
            0.4,
            0.4,
            0.4,
            0.01
        )
    }

    private fun LivingEntity.applyCobweb(level: Int, duration: Int, damage: Float) {
        addStatusEffect(
            StatusEffectInstance(
                ModStatusEffects.COBWEBBED,
                duration,
                level,
                false,
                false,
                false,
            )
        )
        damage(
            damageSources.mobProjectile(this, this@SpiderSilkBolaProjectile.owner as? LivingEntity ?: return),
            damage
        )

    }

    override fun asItemStack(): ItemStack = ItemStack.EMPTY
}