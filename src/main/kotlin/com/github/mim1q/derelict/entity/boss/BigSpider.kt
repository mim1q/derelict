package com.github.mim1q.derelict.entity.boss

import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.*

interface BigSpider {
  val bigSpiderAnimationProperties: BigSpiderAnimationProperties
  private val properties
    get() = bigSpiderAnimationProperties

  fun getYawChangeProgress(tickDelta: Float = 1F) =
    MathHelper.lerp(tickDelta, properties.prevYawChangeProgress, properties.yawChangeProgress)
  fun getYawChangeDelta(tickDelta: Float = 1F) =
    MathHelper.lerp(tickDelta, properties.prevYawChangeDelta, properties.yawChangeDelta)
  fun getSpeedChangeProgress(tickDelta: Float = 1F) =
    MathHelper.lerp(tickDelta, properties.prevSpeedChangeProgress, properties.speedChangeProgress)
  fun getSpeedChangeDelta(tickDelta: Float = 1F) =
    MathHelper.lerp(tickDelta, properties.prevSpeedChangeDelta, properties.speedChangeDelta)
}

class BigSpiderAnimationProperties(private val entity: LivingEntity) {
  var yawChangeProgress: Float = 0.0F
    private set
  var prevYawChangeProgress: Float = 0.0F
    private set
  var yawChangeDelta: Float = 0.0F
    private set
  var prevYawChangeDelta: Float = 0.0F
    private set
  var speedChangeProgress: Float = 0.0F
    private set
  var prevSpeedChangeProgress: Float = 0.0F
    private set
  var speedChangeDelta: Float = 0.0F
    private set
  var prevSpeedChangeDelta: Float = 0.0F
    private set

  fun tick() {
    prevYawChangeDelta = yawChangeDelta
    prevYawChangeProgress = yawChangeProgress
    prevSpeedChangeDelta = speedChangeDelta
    prevSpeedChangeProgress = speedChangeProgress

    if (abs(entity.prevBodyYaw - entity.bodyYaw) < 0.1F) {
      yawChangeDelta = max(yawChangeDelta - 0.1F, 0.0F)
    } else {
      yawChangeDelta = min(yawChangeDelta + 0.1F, 0.5F)
      yawChangeProgress += if (entity.prevBodyYaw > entity.bodyYaw) -0.25F else 0.25F
    }
    val distance = (entity.x - entity.prevX).pow(2) + (entity.z - entity.prevZ).pow(2)
    if (distance <= 0.001) {
      speedChangeDelta = max(speedChangeDelta - 0.1F, 0.0F)
    } else {
      speedChangeDelta = min(speedChangeDelta + 0.1F, 1.0F)
      speedChangeProgress += MathHelper.clamp(sqrt(distance), 0.1, 1.0).toFloat()
    }
  }
}