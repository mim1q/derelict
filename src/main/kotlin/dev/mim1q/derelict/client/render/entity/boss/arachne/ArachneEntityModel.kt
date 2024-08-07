package dev.mim1q.derelict.client.render.entity.boss.arachne

import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.entity.spider.legs.SpiderLegParts
import dev.mim1q.derelict.util.extensions.radians
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
    }

    override fun animateModel(entity: ArachneEntity, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        val animationProgress = entity.age + tickDelta

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