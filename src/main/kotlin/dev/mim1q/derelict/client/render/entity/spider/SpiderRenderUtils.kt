package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.ModelPart
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
