package dev.mim1q.derelict.entity.spider.control

import dev.mim1q.derelict.util.degreesDifference
import dev.mim1q.derelict.util.extensions.degrees
import net.minecraft.entity.ai.control.BodyControl
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.MathHelper.clampAngle
import net.minecraft.util.math.MathHelper.lerpAngleDegrees
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2

class ArachneBodyControl(
    private val entity: MobEntity,
    private val factor: Float = 0.1f
) : BodyControl(entity) {
    override fun tick() {
        val movementVector = Vec3d(entity.x - entity.prevX, 0.0, entity.z - entity.prevZ)
        var targetBodyYaw = atan2(movementVector.z, movementVector.x).toFloat().degrees() - 90F

        val difference = degreesDifference(entity.bodyYaw, targetBodyYaw)
        if (difference > 40) {
            targetBodyYaw = lerpAngleDegrees(factor, targetBodyYaw, entity.bodyYaw)
        }

        if (movementVector.horizontalLengthSquared() > 0.0001) {
            entity.setBodyYaw(lerpAngleDegrees(factor, entity.bodyYaw, targetBodyYaw))
            entity.headYaw = clampAngle(entity.headYaw, entity.bodyYaw, entity.maxHeadRotation.toFloat())
        } else {
            entity.setBodyYaw(lerpAngleDegrees(factor, entity.bodyYaw, entity.headYaw))
        }
    }
}