package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.entity.boss.BigSpider
import dev.mim1q.derelict.util.Easing.smoothStep
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAngles
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.acos
import kotlin.math.sin

class ArachneEntityModel<T>(root: ModelPart) : EntityModel<T>(RenderLayer::getEntityCutout)
    where T : LivingEntity,
          T : BigSpider {
    private val body = root.getChild("body")
    private val sternum = body.getChild("sternum")
    private val abdomen = sternum.getChild("abdomen")
    private val eggs = IntRange(0, 16).map { abdomen.getChild("eggs").getChild("egg$it") }
    private val head = body.getChild("head")

    private var additionalBodyHeight = 0.0F

    class BigSpiderLimbParts(
        private val parent: ArachneEntityModel<*>,
        private val defaultYaw: Float,
        private val defaultRoll: Float,
        private val height: Float,
        number: Int,
        right: Boolean
    ) {
        private val prefix = if (right) "right" else "left"
        private val multiplier = if (right) -1 else 1
        private val joint: ModelPart = parent.body.getChild("${prefix}LimbJoint$number")
        private val limb: ModelPart = joint.getChild("${prefix}Limb$number")
        private val forelimb: ModelPart = limb.getChild("${prefix}Forelimb$number")

        fun setAngles(
            yawDegrees: Float, rollDegrees: Float, additionalRollDegrees: Float = 0F, delta: Float = 1F
        ) {
            joint.setPartialAnglesDegrees(yaw = yawDegrees * multiplier, delta = delta)
            limb.setPartialAnglesDegrees(
                roll = (-rollDegrees - additionalRollDegrees * delta) * multiplier,
                delta = delta
            )
            val jointToGround = (parent.additionalBodyHeight / 16F) + height + LIMB_LENGTH * sin(rollDegrees.radians())
            val forelimbAngle = acos(MathHelper.clamp(jointToGround / FORELIMB_LENGTH, -1.0F, 1.0F))
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

        companion object {
            const val LIMB_LENGTH = 22 / 16F
            const val FORELIMB_LENGTH = 30 / 16F
        }
    }

    private val leftLegs = arrayOf(
        BigSpiderLimbParts(this, 75F, 15F, 13 / 16F, 0, false),
        BigSpiderLimbParts(this, 30F, 25F, 15 / 16F, 1, false),
        BigSpiderLimbParts(this, -15F, 20F, 17 / 16F, 2, false),
        BigSpiderLimbParts(this, -50F, 5F, 19 / 16F, 3, false),
    )
    private val rightLegs = arrayOf(
        BigSpiderLimbParts(this, 75F, 15F, 13 / 16F, 0, true),
        BigSpiderLimbParts(this, 30F, 25F, 15 / 16F, 1, true),
        BigSpiderLimbParts(this, -15F, 20F, 17 / 16F, 2, true),
        BigSpiderLimbParts(this, -50F, 5F, 19 / 16F, 3, true),
    )

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun setAngles(
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        head.setAngles(headPitch.radians(), headYaw.radians(), 0F)
    }

    override fun animateModel(entity: T, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        resetRotations()
        val animationProgress = entity.age + tickDelta
        val speedProgress = entity.getSpeedChangeProgress(tickDelta) * 2F
        val speedDelta = entity.getSpeedChangeDelta(tickDelta)
        val yawProgress = entity.getYawChangeProgress(tickDelta)
        val yawDelta = entity.getYawChangeDelta(tickDelta)

        idleAnimation(animationProgress * 0.1F, 1F - speedDelta)
        rotationAnimation(yawProgress, yawDelta)
        walkAnimation(speedProgress, speedDelta)

        eggs.forEachIndexed { index, egg ->
            val speed = 7F + sin(index * 100F) * 3F
            val scale = 1F + sin((animationProgress + index * speed) * 0.25F) * 0.1F
            egg.xScale = scale; egg.yScale = scale; egg.zScale = scale
        }
    }

    private fun resetRotations() {
        body.pivotY = 14F
        abdomen.pitch = 10F.radians()
        additionalBodyHeight = 0F
        leftLegs.forEach { it.resetAngles() }
        rightLegs.forEach { it.resetAngles() }
    }

    private fun idleAnimation(animationProgress: Float, delta: Float) {
        additionalBodyHeight = sin(animationProgress) * 3F * delta
        val additionalRoll = additionalBodyHeight * -2F
        body.pivotY = 14F - additionalBodyHeight
        abdomen.setPartialAnglesDegrees(pitch = sin(animationProgress - 0.5F) * 10F + 10F, delta = delta)
        leftLegs.forEach { it.setAnglesFromDefaults(rollDegrees = additionalRoll, delta = delta) }
        rightLegs.forEach { it.setAnglesFromDefaults(rollDegrees = additionalRoll, delta = delta) }
    }

    private fun walkAnimation(progress: Float, delta: Float) {
        additionalBodyHeight = sin(progress) * 2F * delta
        body.pivotY -= additionalBodyHeight
        body.setPartialAnglesDegrees(
            yaw = sin(progress),
            delta = delta
        )
        abdomen.setPartialAnglesDegrees(
            yaw = sin(progress + 60F.radians()) * 5F,
            pitch = 5F + sin(progress * 2F + 45F.radians()) * 5F,
            delta = delta
        )
        leftLegs.forEachIndexed { index, leg -> walkLeg(leg, progress, delta, index, false) }
        rightLegs.forEachIndexed { index, leg -> walkLeg(leg, progress, delta, index, true) }
    }

    private fun walkLeg(leg: BigSpiderLimbParts, progress: Float, delta: Float, index: Int, right: Boolean) {
        val multiplier = legMultiplier(right, index)
        val offset = index * 15F.radians() + if (right) 5F.radians() else 0F
        val yaw = sin(progress + offset) * 20F
        val roll = multiplier * sin(progress - 100F.radians() + offset).let {
            if (index == 0) it * 15F + multiplier * 15F else it * 5F + multiplier * 5F
        }
        val additionalRoll = smoothStep(multiplier * sin(progress + 90F.radians() + offset) + 0.25F, 0F, 1.25F) * 20F
        leg.setAnglesFromDefaults(yaw * multiplier, roll, additionalRoll, delta = delta)
    }

    private fun rotationAnimation(progress: Float, delta: Float) {
        walkAnimation(progress, delta)
    }

    private fun legMultiplier(right: Boolean, index: Int) = if (right xor (index % 2 == 0)) -1F else 1F
}