package dev.mim1q.derelict.client.render.effect

import dev.mim1q.derelict.Derelict
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity

class SpiderWebModelFeature(
    context: LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>,
) : FeatureRenderer<LivingEntity, EntityModel<LivingEntity>>(context) {

    override fun render(matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, entity: LivingEntity, limbAngle: Float, limbDistance: Float, tickDelta: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        contextModel.render(
            matrices,
            (vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(Derelict.id("textures/entity/effect/spider_web.png")))),
            light,
            OverlayTexture.DEFAULT_UV,
            1f, 1f, 1f, 0.9f,
        )
    }
}