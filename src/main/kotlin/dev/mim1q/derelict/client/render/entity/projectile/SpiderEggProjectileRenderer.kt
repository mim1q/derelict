package dev.mim1q.derelict.client.render.entity.projectile

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.projectile.SpiderEggProjectile
import dev.mim1q.derelict.util.render.entry
import dev.mim1q.derelict.util.render.render
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class SpiderEggProjectileRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SpiderEggProjectile>(ctx) {
    val model = ctx.modelManager.getModel(Derelict.id("block/spider_egg/huge_spider_egg"))

    override fun render(
        entity: SpiderEggProjectile,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
        matrices.entry {
            model?.render(entity.world.random, light, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()))
        }
    }

    override fun getTexture(entity: SpiderEggProjectile): Identifier = Derelict.id("none")
}