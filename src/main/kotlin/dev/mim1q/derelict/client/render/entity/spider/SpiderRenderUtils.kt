package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.Vec3d
import kotlin.math.*

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

class SpiderLegIKSolver(
    private val upperLength: Float,
    private val lowerLength: Float
) {
    private var firstTick = true

    private var yaw = 0f
    private var upperRoll = 0f
    private var lowerRoll = 0f

    private var lastYaw = 0f
    private var lastUpperRoll = 0f
    private var lastLowerRoll = 0f

    fun solve(
        target: Vec3d
    ) {
        lastYaw = yaw
        lastUpperRoll = upperRoll
        lastLowerRoll = lowerRoll

        val lowerRollCosine = ((target.lengthSquared().toFloat() - upperLength * upperLength - lowerLength * lowerLength) / (2f * upperLength * lowerLength)).coerceIn(-1f, 1f)
        val newLowerRoll = asin(lowerRollCosine)
        lowerRoll = newLowerRoll
        val lowerRollSine = sin(newLowerRoll)

        val adjacent = upperLength + lowerLength * lowerRollCosine
        val opposite = lowerLength * lowerRollSine

        val targetHorizontal = target.horizontalLength().toFloat()
        val targetVertical = target.y.toFloat()

        val tangentY = targetVertical * adjacent - targetHorizontal * opposite
        val tangentX = targetHorizontal * adjacent + targetVertical * opposite

        upperRoll = atan2(tangentY, tangentX)
        lastYaw = atan2(targetHorizontal, targetVertical)

        if (firstTick) {
            firstTick = false
            lastYaw = yaw
            lastUpperRoll = upperRoll
            lastLowerRoll = lowerRoll
        }
    }

    fun apply(
        joint: ModelPart,
        upperLimb: ModelPart,
        lowerLimb: ModelPart,
        tickDelta: Float
    ) {
        joint.yaw = Easing.lerp(lastYaw, yaw, tickDelta)
        upperLimb.roll = Easing.lerp(lastUpperRoll, upperRoll, tickDelta)
        lowerLimb.roll = Easing.lerp(lastLowerRoll, lowerRoll, tickDelta)
    }
}