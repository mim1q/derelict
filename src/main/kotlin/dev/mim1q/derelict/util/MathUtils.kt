package dev.mim1q.derelict.util

import net.minecraft.util.math.MathHelper

val TWO_PI = MathHelper.PI * 2f

fun lerpAngleRadians(start: Float, end: Float, delta: Float): Float = start + delta * wrapRadians(end - start)

fun wrapRadians(radians: Float) = (radians % TWO_PI).let {
    if (it >= MathHelper.PI) it - TWO_PI
    else if (it < -MathHelper.PI) it + TWO_PI
    else it
}