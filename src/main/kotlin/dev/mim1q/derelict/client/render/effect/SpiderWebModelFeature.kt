package dev.mim1q.derelict.client.render.effect

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModStatusEffects
import dev.mim1q.derelict.init.component.ModCardinalComponents.getClientSyncedEffectAmplifier
import dev.mim1q.derelict.init.component.ModCardinalComponents.hasClientSyncedEffect
import dev.mim1q.derelict.util.render.entry
import dev.mim1q.gimm1q.client.render.overlay.ModelOverlayFeatureRenderer
import dev.mim1q.gimm1q.client.render.overlay.ModelOverlayVertexConsumer
import dev.mim1q.gimm1q.interpolation.Easing.lerp
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.RotationAxis

val SPIDER_WEB_TEXTURE_SPARSE = Derelict.id("textures/entity/effect/spider_web_sparse.png")
val SPIDER_WEB_TEXTURE_MEDIUM = Derelict.id("textures/entity/effect/spider_web.png")
val SPIDER_WEB_TEXTURE_DENSE = Derelict.id("textures/entity/effect/spider_web_dense.png")

class SpiderWebModelFeature(
    context: LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>,
) : ModelOverlayFeatureRenderer<LivingEntity, EntityModel<LivingEntity>>(
    context,
    { it.hasClientSyncedEffect(ModStatusEffects.COBWEBBED) },
    vertexConsumerPicker@{ entity, vertexConsumers ->
        val texture = when (entity.getClientSyncedEffectAmplifier(ModStatusEffects.COBWEBBED)) {
            2 -> SPIDER_WEB_TEXTURE_DENSE
            1 -> SPIDER_WEB_TEXTURE_MEDIUM
            else -> SPIDER_WEB_TEXTURE_SPARSE
        }
        return@vertexConsumerPicker ModelOverlayVertexConsumer
            .of(vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)))
            .textureSize(64)
            .offset(0.501f)
            .skipPlanes()
    },
    true
) {
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
        super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch)

        if (!entity.hasClientSyncedEffect(ModStatusEffects.COBWEBBED)) return

        val blockRenderer = MinecraftClient.getInstance().blockRenderManager
        val cobweb = ModBlocksAndItems.CORNER_COBWEB.defaultState

        matrices.entry {
            multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - lerp(entity.prevBodyYaw, entity.bodyYaw, tickDelta) + entity.id * 777))
            if (entity.height >= 0.7 && entity.width <= 4.0) repeat(3) {
                multiply(RotationAxis.POSITIVE_Y.rotationDegrees(120f))
                matrices.entry {
                    translate(-0.5, 0.6, -0.45 * (entity.width + 2))
                    blockRenderer.renderBlock(
                        cobweb, entity.blockPos, entity.world, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), true,
                        entity.world.random
                    )
                }
            }
        }
    }
}