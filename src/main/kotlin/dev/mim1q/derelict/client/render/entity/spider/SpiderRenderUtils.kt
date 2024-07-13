package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.util.Easing.smoothStep
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAngles
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.MathHelper
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

fun walkSpiderLegs(legs: Array<ModelPart>, progress: Float, limbDistance: Float) {
    walkSpiderLeg(legs, 0, 60f, progress, 0f, 0.4f * limbDistance)
    walkSpiderLeg(legs, 1, 25f, progress, 90f, 0.4f * limbDistance)
    walkSpiderLeg(legs, 2, -15f, progress, 15f, 0.5f * limbDistance)
    walkSpiderLeg(legs, 3, -35f, progress, 105f, 0.6f * limbDistance)

    walkSpiderLeg(legs, 4, -60f, progress, 10f, 0.4f * limbDistance)
    walkSpiderLeg(legs, 5, -25f, progress, 100f, 0.4f * limbDistance)
    walkSpiderLeg(legs, 6, 15f, progress, 25f, 0.5f * limbDistance)
    walkSpiderLeg(legs, 7, 35f, progress, 115f, 0.6f * limbDistance)
}

private fun walkSpiderLeg(legs: Array<ModelPart>, index: Int, defaultAngle: Float, progress: Float, offset: Float, multiplier: Float) {
    val leg = legs[index]
    leg.yaw = defaultAngle.radians() + sin((progress + offset.radians())) * multiplier
    leg.roll = max(0f, cos((progress + (offset - 35f).radians())) * multiplier)
    leg.roll *= if (index < 4) -1 else 1
}

fun bigSpiderWalkAnimation(
    body: ModelPart,
    abdomen: ModelPart?,
    leftLegs: Array<BigSpiderLimb>,
    rightLegs: Array<BigSpiderLimb>,
    progress: Float,
    delta: Float
) {
    body.pivotY -= sin(progress) * 2F * delta
    body.setPartialAnglesDegrees(
        yaw = sin(progress),
        delta = delta
    )
    abdomen?.setPartialAnglesDegrees(
        yaw = sin(progress + 60F.radians()) * 5F,
        pitch = 5F + sin(progress * 2F + 45F.radians()) * 5F,
        delta = delta
    )
    leftLegs.forEachIndexed { index, leg -> leg.walkLeg(progress, delta, index, false) }
    rightLegs.forEachIndexed { index, leg -> leg.walkLeg(progress, delta, index, true) }
}

class BigSpiderLimb(
    body: ModelPart,
    private val defaultYaw: Float,
    private val defaultRoll: Float,
    private val height: () -> Float,
    private val limbLength: Float,
    private val forelimbLength: Float,
    number: Int,
    right: Boolean
) {
    private val prefix = if (right) "right" else "left"
    private val multiplier = if (right) -1 else 1
    val joint: ModelPart = body.getChild("${prefix}_leg_joint$number")
    val limb: ModelPart = joint.getChild("${prefix}_leg$number")
    val forelimb: ModelPart = limb.getChild("${prefix}_leg_front$number")

    private fun setAngles(
        yawDegrees: Float, rollDegrees: Float, additionalRollDegrees: Float = 0F, delta: Float = 1F
    ) {
        joint.setPartialAnglesDegrees(yaw = yawDegrees * multiplier, delta = delta)
        limb.setPartialAnglesDegrees(
            roll = (-rollDegrees - additionalRollDegrees * delta) * multiplier,
            delta = delta
        )
        val jointToGround = height() + limbLength * sin(rollDegrees.radians())
        val forelimbAngle = acos(MathHelper.clamp(jointToGround / forelimbLength, -1.0F, 1.0F))
        forelimb.setPartialAngles(
            roll = (MathHelper.HALF_PI + rollDegrees.radians() - forelimbAngle) * multiplier,
            delta = delta
        )
    }

    fun setAnglesFromDefaults(
        yawDegrees: Float = 0F, rollDegrees: Float = 0F, additionalRollDegrees: Float = 0F, delta: Float = 1F
    ) = setAngles(
        defaultYaw + yawDegrees, defaultRoll + rollDegrees, additionalRollDegrees, delta
    )

    fun resetAngles() = setAngles(defaultYaw, defaultRoll)

    fun walkLeg(progress: Float, delta: Float, index: Int, right: Boolean) {
        val multiplier = legMultiplier(right, index)
        val offset = index * 15F.radians() + if (right) 5F.radians() else 0F
        val yaw = sin(progress + offset) * 20F
        val roll = multiplier * sin(progress - 100F.radians() + offset).let {
            if (index == 0) it * 15F + multiplier * 15F else it * 5F + multiplier * 5F
        }
        val additionalRoll = smoothStep(multiplier * sin(progress + 90F.radians() + offset) + 0.25F, 0F, 1.25F) * 30F
        setAnglesFromDefaults(yaw * multiplier, roll, additionalRoll, delta = delta)
    }

    private fun legMultiplier(right: Boolean, index: Int) = if (right xor (index % 2 == 0)) -1F else 1F
}