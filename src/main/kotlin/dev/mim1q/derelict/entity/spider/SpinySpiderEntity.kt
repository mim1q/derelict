package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.init.ModStatusEffects
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.PounceAtTargetGoal
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior
import kotlin.math.max

private const val MAX_FUSE_TIME = 30

class SpinySpiderEntity(
    entityType: EntityType<out SpiderEntity>,
    world: World
) : SpiderEntity(entityType, world) {

    override fun initGoals() {
        super.initGoals()

        clearGoals {
            it is PounceAtTargetGoal
        }

        targetSelector.clear { true }

        targetSelector.add(1, RevengeGoal(this, *arrayOfNulls(0)))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(3, ActiveTargetGoal(this, IronGolemEntity::class.java, true))

    }

    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(FUSE_SPEED, -1)
    }

    private var prevFuse = 0f
    private var fuse = 0f

    override fun tick() {
        super.tick()

        val fuseSpeed = dataTracker[FUSE_SPEED]

        prevFuse = fuse
        fuse = max(fuse + fuseSpeed, 0f)

        if (!world.isClient) {
            if (fuseSpeed == -1 && world.getClosestPlayer(this, 2.0) != null) {
                dataTracker[FUSE_SPEED] = 1
                playSound(
                    SoundEvents.ENTITY_TNT_PRIMED,
                    1.0f,
                    1.0f
                )
            } else if (fuseSpeed == 1 && world.getClosestPlayer(this, 4.0) == null) {
                dataTracker[FUSE_SPEED] = -1
            }

            if (fuse > MAX_FUSE_TIME) {
                explode()
            }
        }
    }

    private fun explode() {
        dead = true
        discard()
        world.createExplosion(
            this,
            null,
            object : ExplosionBehavior() {
                override fun canDestroyBlock(explosion: Explosion, world: BlockView, pos: BlockPos, state: BlockState, power: Float): Boolean = false
            },
            pos.add(0.0, 0.5, 0.0),
            0.5f,
            false,
            World.ExplosionSourceType.MOB
        )

        (world as? ServerWorld)?.spawnParticles(
            ItemStackParticleEffect(ParticleTypes.ITEM, Blocks.COBWEB.asItem().defaultStack),
            pos.x + 0.5,
            pos.y + 0.5,
            pos.z + 0.5,
            40,
            0.5,
            0.5,
            0.5,
            0.3
        )

        world.getOtherEntities(this, Box.of(pos, 4.0, 4.0, 4.0)) { it.distanceTo(this) <= 4.0 }.forEach {
            if (it is LivingEntity && it !is SpinySpiderEntity) {
                val currentLevel = it.getStatusEffect(ModStatusEffects.COBWEBBED)?.amplifier ?: -1

                it.addStatusEffect(
                    StatusEffectInstance(
                        ModStatusEffects.COBWEBBED,
                        60,
                        (currentLevel + 1).coerceAtMost(2),
                        true,
                        true,
                        false
                    )
                )
            }
        }
    }

    fun getFuse(tickDelta: Float) = Easing.lerp(prevFuse, fuse, tickDelta) / (MAX_FUSE_TIME - 2f)

    companion object {
        private val FUSE_SPEED: TrackedData<Int> = DataTracker.registerData(SpinySpiderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}