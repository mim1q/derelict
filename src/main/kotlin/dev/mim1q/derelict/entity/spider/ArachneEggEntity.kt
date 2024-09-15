package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import dev.mim1q.derelict.util.entity.tracked
import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World

class ArachneEggEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    var stage by tracked(STAGE)
        private set

    var stageCooldown = 20

    override fun initDataTracker() {
        dataTracker.startTracking(STAGE, 0)
    }

    override fun tick() {
        super.tick()

        if (stageCooldown > 0) {
            stageCooldown--
        }

        if (!world.isClient && age % 5 == 0 && stage < 3) {
            (world as ServerWorld).spawnParticles(
                BlockStateParticleEffect(ParticleTypes.BLOCK, ModBlocksAndItems.SPIDER_EGG_BLOCK.defaultState),
                this.x,
                this.y + 1.2,
                this.z,
                1 + 2 * stage,
                0.8,
                1.2,
                0.8,
                0.05
            )
        }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (stageCooldown <= 0 && !world.isClient && source.attacker is PlayerEntity) {
            if (stage < 4) {
                stage = (stage + 1) % 4
                stageCooldown = 20

                world.playSound(null, blockPos, SoundEvents.ENTITY_SPIDER_HURT, SoundCategory.HOSTILE, 0.5f, 0.5f)
                ScreenShakeUtils.shakeAround(
                    world as ServerWorld,
                    pos,
                    1 + 0.5f * stage,
                    if (stage == 3) 70 else 40,
                    8.0,
                    20.0
                )
                (world as ServerWorld).spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, ModBlocksAndItems.SPIDER_EGG_BLOCK.defaultState),
                    this.x,
                    this.y + 1.2,
                    this.z,
                    80,
                    0.8,
                    1.2,
                    0.8,
                    0.05
                )

                if (stage == 3) {
                    (world as ServerWorld).spawnParticles(
                        ParticleTypes.EXPLOSION_EMITTER,
                        this.x,
                        this.y + 1.2,
                        this.z,
                        3,
                        2.8,
                        2.8,
                        2.8,
                        0.05
                    )
                    playSound(SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f)
                    playSound(SoundEvents.ENTITY_SPIDER_DEATH, 2.0f, 0.2f)

                    val boss = ArachneEntity(ModEntities.ARACHNE, world)
                    boss.setPosition(pos)
                    world.spawnEntity(boss)
                }

                return true
            }
        }

        return super.damage(source, amount)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
    }

    override fun canHit(): Boolean = true
    override fun isCollidable(): Boolean = stage != 3
    override fun collidesWith(other: Entity): Boolean = isCollidable


    companion object {
        private val STAGE: TrackedData<Int> =
            DataTracker.registerData(ArachneEggEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}