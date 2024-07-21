package dev.mim1q.derelict.util.extensions

import net.minecraft.entity.Entity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

fun Float.radians() = this * MathHelper.RADIANS_PER_DEGREE
fun Float.degrees() = this * MathHelper.DEGREES_PER_RADIAN
fun Double.radians() = this * MathHelper.RADIANS_PER_DEGREE.toDouble()
fun Double.degrees() = this * MathHelper.DEGREES_PER_RADIAN.toDouble()

fun Entity.getLocallyOffsetPos(offset: Vec3d) = pos.add(offset.rotateY(-bodyYaw.radians()))