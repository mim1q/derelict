package dev.mim1q.derelict.client.render.block

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.block.cobweb.SpiderEggBlock
import dev.mim1q.derelict.block.cobweb.SpiderEggBlock.SpiderEggBlockEntity
import dev.mim1q.derelict.util.render.render
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.random.Random
import kotlin.math.sin

class SpiderEggClusterRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<SpiderEggBlockEntity> {
    companion object {
        val MODEL_IDS = arrayOf(
            Derelict.id("block/spider_egg/tiny_spider_egg"),
            Derelict.id("block/spider_egg/small_spider_egg"),
            Derelict.id("block/spider_egg/medium_spider_egg"),
            Derelict.id("block/spider_egg/large_spider_egg"),
            Derelict.id("block/spider_egg/huge_spider_egg"),
        )
    }

    private val models = MODEL_IDS.mapNotNull {
        ctx.renderManager.models.modelManager.getModel(it)
    }

    override fun render(
        entity: SpiderEggBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val block = entity.cachedState.block
        if (block is SpiderEggBlock) {
            matrices.translate(0.5, 0.0, 0.5)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.pos.hashCode() % 4) * 90f))
            matrices.translate(-0.5, 0.0, -0.5)
            val random = entity.world?.random ?: return
            val addedTime = (entity.pos.hashCode() % 100) * 777
            val time = (entity.world?.time ?: return) + tickDelta + addedTime
            if (block.big) {
                val offset = entity.cachedState.getModelOffset(entity.world, entity.pos)
                matrices.translate(offset.x, offset.y, offset.z)
                renderBig(time, random, matrices, vertexConsumers, light)
            } else {
                renderCluster(time, random, matrices, vertexConsumers, light)
            }
        }
    }

    private fun renderBig(time: Float, random: Random, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int) {
        renderEgg(random, matrices, consumers, light, 0.5f, 0f, 0.5f, 4, time)
    }

    private fun renderCluster(time: Float, random: Random, matrices: MatrixStack, consumers: VertexConsumerProvider, light: Int) {
        renderEgg(random, matrices, consumers, light, 0.9f, 0f, 0.5f, 0, time)
        renderEgg(random, matrices, consumers, light, 0.1f, 0f, 0.1f, 1, time + 10)
        renderEgg(random, matrices, consumers, light, 0.5f, 0f, 0.8f, 2, time + 20)
        renderEgg(random, matrices, consumers, light, 0.7f, 0f, 0.3f, 3, time + 30)
    }

    private fun renderEgg(
        random: Random,
        matrices: MatrixStack,
        consumers: VertexConsumerProvider,
        light: Int,
        x: Float, y: Float, z: Float,
        index: Int,
        time: Float
    ) {
        matrices.push()
        matrices.translate(x, y, z)
        val scale = 0.9f + 0.15f * sin(time * 0.15f)
        matrices.scale(scale, scale, scale)
        models[index].render(random, light, matrices, consumers.getBuffer(RenderLayer.getSolid()))
        matrices.pop()
    }
}