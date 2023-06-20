package com.github.mim1q.derelict.util.extensions

import net.minecraft.util.math.MathHelper

fun Float.radians() = this * MathHelper.RADIANS_PER_DEGREE
fun Float.degrees() = this * MathHelper.DEGREES_PER_RADIAN
fun Double.radians() = this * MathHelper.RADIANS_PER_DEGREE.toDouble()
fun Double.degrees() = this * MathHelper.DEGREES_PER_RADIAN.toDouble()