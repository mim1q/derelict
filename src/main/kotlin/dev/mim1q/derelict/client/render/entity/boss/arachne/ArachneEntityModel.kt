package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.client.render.entity.spider.BigSpiderLimb
import dev.mim1q.derelict.client.render.entity.spider.bigSpiderWalkAnimation
import dev.mim1q.derelict.entity.boss.BigSpider
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import kotlin.math.sin

class ArachneEntityModel<T>(root: ModelPart) : EntityModel<T>(RenderLayer::getEntityCutout)
    where T : LivingEntity,
          T : BigSpider {

    companion object {
        const val LIMB_LENGTH = 22 / 16F
        const val FORELIMB_LENGTH = 30 / 16F
    }

    private val body = root.getChild("body")
    private val sternum = body.getChild("sternum")
    private val abdomen = sternum.getChild("abdomen")
    private val eggs = IntRange(0, 16).map { abdomen.getChild("eggs").getChild("egg$it") }
    private val head = body.getChild("head")

    private var additionalBodyHeight = 0.0F

    //@formatter:off
    private val leftLegs = arrayOf(
        BigSpiderLimb(body,  75F, 15F, { (additionalBodyHeight + 13) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 0, false),
        BigSpiderLimb(body,  30F, 25F, { (additionalBodyHeight + 15) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 1, false),
        BigSpiderLimb(body, -15F, 20F, { (additionalBodyHeight + 17) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 2, false),
        BigSpiderLimb(body, -50F, 5F,  { (additionalBodyHeight + 19) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 3, false),
    )
    private val rightLegs = arrayOf(
        BigSpiderLimb(body,  75F, 15F, { (additionalBodyHeight + 13) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 0, true),
        BigSpiderLimb(body,  30F, 25F, { (additionalBodyHeight + 15) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 1, true),
        BigSpiderLimb(body, -15F, 20F, { (additionalBodyHeight + 17) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 2, true),
        BigSpiderLimb(body, -50F, 5F,  { (additionalBodyHeight + 19) / 16F }, LIMB_LENGTH, FORELIMB_LENGTH, 3, true),
    )
    //@formatter:on

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
        bigSpiderWalkAnimation(body, abdomen, leftLegs, rightLegs, speedProgress, speedDelta) { if (it == 0) 2f else 1f }

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

    private fun rotationAnimation(progress: Float, delta: Float) {
        bigSpiderWalkAnimation(body, abdomen, leftLegs, rightLegs, progress, delta)
    }
}