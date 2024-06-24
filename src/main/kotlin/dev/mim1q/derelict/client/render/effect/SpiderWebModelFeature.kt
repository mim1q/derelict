package dev.mim1q.derelict.client.render.effect

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.util.render.entry
import dev.mim1q.gimm1q.interpolation.Easing.lerp
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.RotationAxis

class SpiderWebModelFeature(
    context: LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>,
) : FeatureRenderer<LivingEntity, EntityModel<LivingEntity>>(context) {

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: LivingEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
//        if (!entity.hasStatusEffect(ModStatusEffects.WEBBED)) return

        val blockRenderer = MinecraftClient.getInstance().blockRenderManager
        val cobweb = ModBlocksAndItems.CORNER_COBWEB.defaultState

        matrices.entry {
            multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - lerp(entity.prevBodyYaw, entity.bodyYaw, tickDelta) + entity.id * 777))
            if (entity.height in 0.7..3.0) repeat(3) {
                multiply(RotationAxis.POSITIVE_Y.rotationDegrees(120f))
                matrices.entry {
                    translate(-0.5, 0.6, -0.5 * (entity.width + 2))
                    blockRenderer.renderBlock(
                        cobweb, entity.blockPos, entity.world, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), true,
                        entity.world.random
                    )
                }
            }
        }

        contextModel.render(
            matrices,
            WrappingVertexConsumer(
                vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(Derelict.id("textures/entity/effect/spider_web.png"))),
                0.25f,
                true
            ),
            light,
            OverlayTexture.DEFAULT_UV,
            1f, 1f, 1f, 0.9f,
        )
    }
}