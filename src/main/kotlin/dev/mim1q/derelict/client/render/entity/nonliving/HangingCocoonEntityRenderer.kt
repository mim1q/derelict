package dev.mim1q.derelict.client.render.entity.nonliving

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.nonliving.HangingCocoonEntity
import dev.mim1q.derelict.entity.nonliving.HangingCocoonEntity.Companion.BROKEN
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.util.render.entry
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import kotlin.math.sin

class HangingCocoonEntityRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<HangingCocoonEntity>(ctx) {
    private val modelPart = ctx.getPart(ModRender.HANGING_COCOON_LAYER)

    override fun render(
        entity: HangingCocoonEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.entry {
            scale(-1f, -1f, 1f)

            translate(0.0, -2.5, 0.0)
            val animationProgress = entity.age + tickDelta
            val punchYaw = entity.punchYaw.update(animationProgress)
            val punchDistance = entity.punchDistance.update(animationProgress)

            multiply(RotationAxis.POSITIVE_Y.rotation(punchYaw))
            multiply(RotationAxis.POSITIVE_X.rotation(punchDistance * -sin((entity.punchTime + tickDelta) * 0.2f)))
            multiply(RotationAxis.POSITIVE_Y.rotation(-punchYaw))

            translate(0.0, 2.5, 0.0)

            translate(0.0, -2.501, -0.0)

            modelPart.render(
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getEntityCutout(getTexture(entity))),
                light,
                OverlayTexture.DEFAULT_UV
            )
        }
    }

    override fun getTexture(entity: HangingCocoonEntity): Identifier =
        if (entity.dataTracker[BROKEN]) BROKEN_TEXTURE else TEXTURE

    companion object {
        val TEXTURE = Derelict.id("textures/entity/hanging_cocoon/hanging_cocoon.png")
        val BROKEN_TEXTURE = Derelict.id("textures/entity/hanging_cocoon/hanging_cocoon_broken.png")

        fun createTexturedModelData(): TexturedModelData = ModelData().let {
            it.root.apply {
                addChild("root", ModelPartBuilder.create().uv(0, -10).cuboid(-5F, 4F, -5F, 0F, 28F, 10F).uv(20, -10).cuboid(5F, 4F, -5F, 0F, 28F, 10F).uv(20, 28).cuboid(-5F, 4F, 5F, 10F, 28F, 0F).uv(30, 18).cuboid(-5F, 4F, -5F, 10F, 0F, 10F).uv(30, 28).cuboid(-5F, 32F, -5F, 10F, 0F, 10F).uv(0, 28).cuboid(-5F, 4F, -5F, 10F, 28F, 0F), ModelTransform.pivot(0F, 8F, 0F)).apply {
                    addChild("cube_r1", ModelPartBuilder.create().uv(48, 12).cuboid(-1F, 2F, 0F, 1F, 6F, 0F).uv(40, 0).cuboid(-5F, 7F, 0F, 10F, 6F, 0F), ModelTransform.of(0F, -9F, 0F, 0F, -1.1345F, 0F))
                    addChild("cube_r2", ModelPartBuilder.create().uv(40, 6).cuboid(-5F, 2F, 0F, 10F, 6F, 0F), ModelTransform.of(0F, -4F, 0F, 0F, 0.48F, 0F))
                }
            }
            TexturedModelData.of(it, 64, 64)
        }
    }
}