package com.github.mim1q.derelict.entity.boss

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.control.BodyControl
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper.clamp
import net.minecraft.world.World
import kotlin.math.*

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world) {
  private var yawChangeProgress = 0.0F
  private var prevYawChangeProgress = 0.0F
  private var yawChangeDelta = 0.0F
  private var prevYawChangeDelta = 0.0F

  private var speedChangeProgress = 0.0F
  private var prevSpeedChangeProgress = 0.0F
  private var speedChangeDelta = 0.0F
  private var prevSpeedChangeDelta = 0.0F

  init {
    stepHeight = 2F
  }
  override fun initGoals() {
    goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
    goalSelector.add(4, LookAroundGoal(this))
    goalSelector.add(2, WanderAroundGoal(this, 0.4))
    goalSelector.add(2, WanderAroundFarGoal(this, 0.4))
    goalSelector.add(0, MeleeAttackGoal(this, 0.6, true))
    targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, false))
  }

  override fun tick() {
    super.tick()
    prevYawChangeDelta = yawChangeDelta
    prevYawChangeProgress = yawChangeProgress
    prevSpeedChangeDelta = speedChangeDelta
    prevSpeedChangeProgress = speedChangeProgress

    if (abs(prevBodyYaw - bodyYaw) < 0.1F) {
      yawChangeDelta = max(yawChangeDelta - 0.1F, 0.0F)
    } else {
      yawChangeDelta = min(yawChangeDelta + 0.1F, 0.5F)
      yawChangeProgress += if (prevBodyYaw > bodyYaw) -0.25F else 0.25F
    }
    val distance = (x - prevX).pow(2) + (z - prevZ).pow(2)
    if (distance <= 0.001) {
      speedChangeDelta = max(speedChangeDelta - 0.1F, 0.0F)
    } else {
      speedChangeDelta = min(speedChangeDelta + 0.1F, 1.0F)
      speedChangeProgress += clamp(sqrt(distance), 0.1, 1.0).toFloat()
    }
  }

  fun getYawChangeProgress(tickDelta: Float = 1F) = prevYawChangeProgress + (yawChangeProgress - prevYawChangeProgress) * tickDelta
  fun getYawChangeDelta(tickDelta: Float = 1F) = prevYawChangeDelta + (yawChangeDelta - prevYawChangeDelta) * tickDelta
  fun getSpeedChangeProgress(tickDelta: Float = 1F) = prevSpeedChangeProgress + (speedChangeProgress - prevSpeedChangeProgress) * tickDelta
  fun getSpeedChangeDelta(tickDelta: Float = 1F) = prevSpeedChangeDelta + (speedChangeDelta - prevSpeedChangeDelta) * tickDelta

  override fun createBodyControl(): BodyControl = ArachneBodyControl(this)

  companion object {
    fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
      .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
  }
}