package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import dev.mim1q.derelict.util.entity.tracked
import dev.mim1q.gimm1q.interpolation.Easing
import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.world.World

class ArachneEggEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    var stage by tracked(STAGE)
        private set


    private var lastAnimationTime = 0
    private var animationTime = 0
    private var stageCooldown = 20
    private var lastTimeWithSpiderInArena = 0L

    override fun initDataTracker() {
        dataTracker.startTracking(STAGE, 0)
    }

    override fun tick() {
        super.tick()

        lastAnimationTime = animationTime
        animationTime += stage + 1
        if (stage == 3) {
            intersectionChecked = false
            animationTime = lastAnimationTime

            if (!world.isClient() && age % 20 == 0) {
                if (world.getClosestEntity(
                        ArachneEntity::class.java,
                        TargetPredicate.DEFAULT,
                        null,
                        x, y, z,
                        Box.of(pos, 20.0, 6.0, 20.0)
                    ) != null
                ) {
                    lastTimeWithSpiderInArena = world.time
                }

                if (world.time - lastTimeWithSpiderInArena > 20 * Derelict.CONFIG.arachneEggRespawnTime()) {
                    breakEggStage()
                }
            }


        } else {
            intersectionChecked = true
        }

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

    fun getAnimationTime(delta: Float) = Easing.lerp(lastAnimationTime.toFloat(), animationTime.toFloat(), delta)

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (world.isClient) {
            return super.damage(source, amount)
        }

        val player = source.attacker
        if (stageCooldown <= 0 && player is ServerPlayerEntity) {
            if (stage < 3 && canBreakEggIfAdvancementMet(player, Derelict.CONFIG.arachneEggAdvancement())) {
                breakEggStage()
                return true
            } else {
                return false
            }
        }

        return super.damage(source, amount)
    }

    private fun canBreakEggIfAdvancementMet(player: ServerPlayerEntity, id: String): Boolean {
        if (id.isBlank()) return true
        val identifier = Identifier.tryParse(id) ?: return true
        val advancement = world.server?.advancementLoader?.get(identifier) ?: return true
        if (player.advancementTracker.getProgress(advancement).isDone) {
            return true
        } else {
            player.sendMessage(
                Text.translatable(
                    "text.derelict.spider_egg_required_advancement",
                    advancement.display?.description ?: ""
                ),
                true
            )
            return false
        }
    }

    private fun breakEggStage() {
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
                0.6,
                1.0,
                0.6,
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
                boss.spawnPos = blockPos.south(4)
                boss.setPosition(pos)
                world.spawnEntity(boss)
            }

            world.getEntitiesByClass(LivingEntity::class.java, this.boundingBox.expand(5.0)) { it !is ArachneEntity }
                .forEach {
                    it.damage(damageSources.generic(), 1f)
                    it.takeKnockback(stage.toDouble(), x - it.x, z - it.z)
                }
        }
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        stage = nbt.getInt("stage")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putInt("stage", stage)
    }

    override fun canHit(): Boolean = stage != 3
    override fun isCollidable(): Boolean = canHit()
    override fun collidesWith(other: Entity): Boolean = canHit()

    companion object {
        private val STAGE: TrackedData<Int> =
            DataTracker.registerData(ArachneEggEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }
}