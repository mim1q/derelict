package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.util.extensions.degrees
import net.minecraft.entity.ai.control.BodyControl
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2

class ArachneBodyControl(private val entity: MobEntity) : BodyControl(entity) {
  override fun tick() {
    val movementVector = Vec3d(entity.x - entity.prevX, 0.0, entity.z - entity.prevZ)
    val targetBodyYaw = atan2(movementVector.z, movementVector.x).toFloat().degrees() - 90F
    if (movementVector.horizontalLengthSquared() > 0.0001) {
      entity.bodyYaw = MathHelper.lerpAngleDegrees(0.1F, entity.bodyYaw, targetBodyYaw)
      entity.headYaw = MathHelper.clampAngle(entity.headYaw, entity.bodyYaw, entity.maxHeadRotation.toFloat())
    } else {
      entity.bodyYaw = MathHelper.lerpAngleDegrees(0.1F, entity.bodyYaw, entity.headYaw)
    }
  }
}