package dev.mim1q.derelict.client.render.entity.projectile

import dev.mim1q.derelict.entity.projectile.SpiderSilkBolaProjectile
import dev.mim1q.derelict.util.render.entry
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis

class SpiderSilkBolaRenderer(context: EntityRendererFactory.Context) : EntityRenderer<SpiderSilkBolaProjectile>(
    context
) {
    private val itemRenderer = context.itemRenderer
    private val stack = Items.COBWEB.defaultStack

    override fun render(
        entity: SpiderSilkBolaProjectile,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        if ((entity.age + tickDelta) < 1.5f) return

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)

        matrices.entry {
            translate(0.0, 0.5, 0.0)
            multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f + entity.yaw))
            multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.pitch))

            itemRenderer.renderItem(
                null,
                stack,
                ModelTransformationMode.NONE,
                false,
                matrices,
                vertexConsumers,
                entity.world,
                light,
                OverlayTexture.DEFAULT_UV,
                0
            )
        }
    }

    override fun getTexture(entity: SpiderSilkBolaProjectile) = Identifier("textures/item/cobweb.png")
}