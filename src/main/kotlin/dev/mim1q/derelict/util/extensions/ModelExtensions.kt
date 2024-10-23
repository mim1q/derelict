package dev.mim1q.derelict.util.extensions

import net.minecraft.client.model.ModelPart
import net.minecraft.util.math.MathHelper

fun ModelPart.setPartialAngles(
    pitch: Float = this.pitch,
    roll: Float = this.roll,
    yaw: Float = this.yaw,
    delta: Float
) {
    setPartialPitch(pitch, delta)
    setPartialRoll(roll, delta)
    setPartialYaw(yaw, delta)
}

fun ModelPart.setPartialAnglesDegrees(
    pitch: Float = this.pitch,
    roll: Float = this.roll,
    yaw: Float = this.yaw,
    delta: Float
) {
    setPartialAngles(pitch.radians(), roll.radians(), yaw.radians(), delta)
}

fun ModelPart.setPartialPitch(
    pitch: Float = this.pitch,
    delta: Float
) {
    this.pitch = MathHelper.lerp(delta, this.pitch, pitch)
}

fun ModelPart.setPartialRoll(
    roll: Float = this.roll,
    delta: Float
) {
    this.roll = MathHelper.lerp(delta, this.roll, roll)
}

fun ModelPart.setPartialYaw(
    yaw: Float = this.yaw,
    delta: Float
) {
    this.yaw = MathHelper.lerp(delta, this.yaw, yaw)
}