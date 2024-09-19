package dev.mim1q.derelict.client.render.entity.spider

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.spider.ArachneEggEntity
import dev.mim1q.derelict.util.render.entry
import dev.mim1q.derelict.util.render.render
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import kotlin.math.sin

class ArachneEggRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<ArachneEggEntity>(ctx) {
    private val blockRenderManager = ctx.blockRenderManager

    override fun getTexture(entity: ArachneEggEntity): Identifier = Derelict.id("none")

    override fun render(
        entity: ArachneEggEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        val world = entity.world
        val model =
            blockRenderManager.models.modelManager.getModel(MODEL_IDS.getOrElse(entity.stage) { MODEL_IDS[0] }) ?: return

        val time = entity.getAnimationTime(tickDelta) * 0.15f

        matrices.entry {
            translate(0.5, 0.0, 0.5)
            val xzScale = 0.9f + 0.05f * sin(time)
            val yScale = 0.9f + 0.07f * sin(time - 1f)
            scale(xzScale, yScale, xzScale)
            translate(-0.5 - 0.5 / xzScale, 1.01, -0.5 - 0.5 / xzScale)


            model.render(
                world.random,
                light,
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getCutout())
            )
        }
    }

    companion object {
        val MODEL_IDS = arrayOf(
            Derelict.id("block/special/arachne_egg"),
            Derelict.id("block/special/arachne_egg_1"),
            Derelict.id("block/special/arachne_egg_2"),
            Derelict.id("block/special/arachne_egg_broken")
        )
    }
}