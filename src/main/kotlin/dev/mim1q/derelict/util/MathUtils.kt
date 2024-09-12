package dev.mim1q.derelict.util

import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import java.lang.Math.min
import kotlin.math.abs

val TWO_PI = MathHelper.PI * 2f

fun lerpAngleRadians(start: Float, end: Float, delta: Float): Float = start + delta * wrapRadians(end - start)

fun wrapRadians(radians: Float) = (radians % TWO_PI).let {
    if (it >= MathHelper.PI) it - TWO_PI
    else if (it < -MathHelper.PI) it + TWO_PI
    else it
}

fun degreesDifference(a: Float, b: Float): Float = min(abs(a - b), 360 - abs(a - b))

fun wrapDegrees(from: Float, to: Float, max: Float): Float {
    var f = MathHelper.wrapDegrees(to - from)
    if (f > max) f = max
    if (f < -max) f = -max

    var g = from + f
    if (g < 0.0f) g += 360.0f
    else if (g > 360.0f) g -= 360.0f

    return g
}

fun <T> Map<T, Int>.pickWeightedRandom(random: Random): T {
    var totalWeight = 0
    val keys = this.keys
    for (key in keys) {
        totalWeight += this[key]!!
    }
    val randomWeight = random.nextInt(totalWeight)
    var currentWeight = 0
    for (key in keys) {
        currentWeight += this[key]!!
        if (currentWeight >= randomWeight) {
            return key
        }
    }
    return keys.first()
}