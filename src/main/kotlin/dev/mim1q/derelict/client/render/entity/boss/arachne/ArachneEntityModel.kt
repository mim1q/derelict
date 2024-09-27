package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.entity.spider.legs.SpiderLegParts
import dev.mim1q.derelict.util.extensions.radians
import dev.mim1q.derelict.util.extensions.setPartialAnglesDegrees
import dev.mim1q.gimm1q.interpolation.Easing
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.sin

class ArachneEntityModel(root: ModelPart) : EntityModel<ArachneEntity>(RenderLayer::getEntityCutout) {

    companion object {
        const val LIMB_LENGTH = 22 / 16F
        const val FORELIMB_LENGTH = 30 / 16F
    }

    private val body = root.getChild("body")
    private val sternum = body.getChild("sternum")
    private val abdomen = sternum.getChild("abdomen")
    private val eggs = IntRange(0, 16).map { abdomen.getChild("eggs").getChild("egg$it") }
    private val head = body.getChild("head")
    private val leftFang = head.getChild("left_fang")
    private val rightFang = head.getChild("right_fang")

    val legs = SpiderLegParts.createArray(body)

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
        entity: ArachneEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        head.setAngles(headPitch.radians(), headYaw.radians(), 0F)

        val screaming = entity.legsRaisedAnimation.update(animationProgress)
        val shaking = entity.shakingAnimation.update(animationProgress)

        legs[0].upper.setPartialAnglesDegrees(0f, -70f, 0f, screaming)
        legs[0].lower.setPartialAnglesDegrees(0f, 50f, 0f, screaming)
        legs[0].joint.setPartialAnglesDegrees(0f, 0f, 60f, screaming)

        legs[4].upper.setPartialAnglesDegrees(0f, 70f, 0f, screaming)
        legs[4].lower.setPartialAnglesDegrees(0f, -50f, 0f, screaming)
        legs[4].joint.setPartialAnglesDegrees(0f, 0f, -60f, screaming)

        body.yaw += sin(x = animationProgress * 3f) * 1f.radians() * shaking
        abdomen.yaw += sin(animationProgress * 3f - 1f) * 5f.radians() * shaking
        head.yaw += sin(animationProgress * 5f - 1f) * 2f.radians() * shaking

        val leftLegStomp = entity.leftLegStomp.update(animationProgress)
        val rightLegStomp = entity.rightLegStomp.update(animationProgress)

        legs[0].upper.roll += 60f.radians() * leftLegStomp
        legs[0].joint.yaw += 20f.radians() * leftLegStomp
        legs[4].upper.roll -= 60f.radians() * rightLegStomp
        legs[4].joint.yaw -= 20f.radians() * rightLegStomp

        val fangAnimation = entity.fangsAnimation.update(animationProgress)
        leftFang.roll += fangAnimation * 20f.radians()
        leftFang.pitch -= fangAnimation * 60f.radians()
        rightFang.roll -= fangAnimation * 20f.radians()
        rightFang.pitch -= fangAnimation * 60f.radians()
    }

    override fun animateModel(entity: ArachneEntity, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        val animationProgress = entity.age + tickDelta

        body.resetTransform()
        abdomen.resetTransform()
        leftFang.resetTransform()
        rightFang.resetTransform()

        eggs.forEachIndexed { index, egg ->
            val speed = 7F + sin(index * 100F) * 3F
            val scale = 1F + sin((animationProgress + index * speed) * 0.25F) * 0.1F
            egg.xScale = scale; egg.yScale = scale; egg.zScale = scale
        }

        legs.forEachIndexed { index, it ->
            it.applyIk(entity.legController.getIk(index), Easing.lerp(entity.prevBodyYaw, entity.bodyYaw, tickDelta), tickDelta)
        }
    }
}