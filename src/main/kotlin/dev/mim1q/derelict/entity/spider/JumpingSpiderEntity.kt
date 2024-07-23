package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.spider.control.ArachneBodyControl
import dev.mim1q.derelict.util.extensions.degrees
import dev.mim1q.gimm1q.interpolation.AnimatedProperty
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.control.BodyControl
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.PounceAtTargetGoal
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.MathHelper.clamp
import net.minecraft.util.math.MathHelper.lerpAngleDegrees
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.atan2
import kotlin.math.max

class JumpingSpiderEntity(
    entityType: EntityType<out JumpingSpiderEntity>,
    world: World
) : SpiderEntity(entityType, world) {
    var jumpAttackCooldown = 40
    val jumpChargeAnimation = AnimatedProperty(0f, Easing::easeOutCubic)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(JUMP_CHARGING, false)
        dataTracker.startTracking(TARGET_ID, -1)
    }

    override fun initGoals() {
        super.initGoals()

        clearGoals {
            it is PounceAtTargetGoal
        }

        goalSelector.add(0, JumpingSpiderJumpAttack())
    }

    override fun playStepSound(pos: BlockPos, state: BlockState) {
        if (!dataTracker[JUMP_CHARGING]) {
            super.playStepSound(pos, state)
        }
    }

    override fun tick() {
        super.tick()

        if (!world.isClient) {
            jumpAttackCooldown = max(0, jumpAttackCooldown - 1)
        } else {
            if (dataTracker[JUMP_CHARGING]) {
                jumpChargeAnimation.transitionTo(1f, 20f)
            } else {
                jumpChargeAnimation.transitionTo(0f, 20f)
            }
        }

        val isChargingJump = dataTracker[JUMP_CHARGING]
        val trackedTarget = world.getEntityById(dataTracker[TARGET_ID])

        if (isChargingJump && trackedTarget != null) {
            setPos(MathHelper.lerp(0.01, prevX, x), y, MathHelper.lerp(0.01, prevZ, z))
            bodyYaw = lerpAngleDegrees(0.2f, prevBodyYaw, atan2(trackedTarget.z - z, trackedTarget.x - x).degrees().toFloat() - 90f)
        }
    }

    override fun setTarget(target: LivingEntity?) {
        super.setTarget(target)
        dataTracker[TARGET_ID] = target?.id ?: -1
    }

    override fun createBodyControl(): BodyControl = ArachneBodyControl(this, 0.7f)

    companion object {
        val JUMP_CHARGING: TrackedData<Boolean> = DataTracker.registerData(JumpingSpiderEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val TARGET_ID: TrackedData<Int> = DataTracker.registerData(JumpingSpiderEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
    }

    inner class JumpingSpiderJumpAttack : Goal() {
        override fun canStart(): Boolean = target != null && target!!.y <= y && jumpAttackCooldown <= 0 && random.nextFloat() < 0.2f
        override fun shouldContinue(): Boolean = target != null && ticks < 15
        private var ticks = 0

        init {
            this.controls = EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP)
        }

        override fun tick() {
            ++ticks
        }

        override fun stop() {
            val currentTarget = target
            if (currentTarget != null) {
                playSound(SoundEvents.ENTITY_PHANTOM_FLAP, 1.0f, 1.4f)
                velocity = currentTarget.eyePos.subtract(eyePos)
                    .let { Vec3d(it.x, clamp(it.y, 0.0, 0.2), it.z) }.normalize().multiply(2.0).add(0.0, 0.2, 0.0)

                velocityModified = true
                velocityDirty = true
            }

            dataTracker[JUMP_CHARGING] = false
            jumpAttackCooldown = 40 + random.nextInt(100)
        }

        override fun start() {
            playSound(SoundEvents.ENTITY_SPIDER_HURT, 1.0f, 1.5f)
            ticks = 0
            dataTracker[JUMP_CHARGING] = true
        }
    }
}