package dev.mim1q.derelict.util.render

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random

inline fun MatrixStack.entry(setup: MatrixStack.() -> Unit) {
    push()
    setup()
    pop()
}

fun BakedModel.render(
    random: Random,
    light: Int,
    matrices: MatrixStack,
    buffer: VertexConsumer
) {
    for (direction in Direction.entries) {
        val quads = this.getQuads(null, direction, random)
        renderBakedQuads(quads, matrices, buffer, light)
    }

    val noDirectionQuads = this.getQuads(null, null, random)
    renderBakedQuads(noDirectionQuads, matrices, buffer, light)
}

private fun renderBakedQuads(
    quads: List<BakedQuad>,
    matrices: MatrixStack,
    buffer: VertexConsumer,
    light: Int
) {
    for (quad in quads) {
        buffer.quad(
            matrices.peek(),
            quad,
            floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f),
            1f, 1f, 1f,
            intArrayOf(light, light, light, light),
            OverlayTexture.DEFAULT_UV,
            false
        )
    }
}