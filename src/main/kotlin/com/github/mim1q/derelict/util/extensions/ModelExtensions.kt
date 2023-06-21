package com.github.mim1q.derelict.util.extensions

import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.MathHelper

fun ModelPart.setPartialAngles(
  pitch: Float = this.pitch,
  roll: Float = this.roll,
  yaw: Float = this.yaw,
  delta: Float
) {
  this.pitch = MathHelper.lerp(delta, this.pitch, pitch)
  this.roll = MathHelper.lerp(delta, this.roll, roll)
  this.yaw = MathHelper.lerp(delta, this.yaw, yaw)
}

fun ModelPart.setPartialAnglesDegrees(
  pitch: Float = this.pitch,
  roll: Float = this.roll,
  yaw: Float = this.yaw,
  delta: Float
) {
  setPartialAngles(pitch.radians(), roll.radians(), yaw.radians(), delta)
}