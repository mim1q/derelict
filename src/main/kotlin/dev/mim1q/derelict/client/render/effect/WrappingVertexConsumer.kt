package dev.mim1q.derelict.client.render.effect

import net.minecraft.client.render.VertexConsumer

class WrappingVertexConsumer(private val inner: VertexConsumer) : VertexConsumer by inner {
    override fun vertex(
        x: Float, y: Float, z: Float,
        red: Float, green: Float, blue: Float, alpha: Float,
        u: Float, v: Float,
        overlay: Int,
        light: Int,
        normalX: Float, normalY: Float, normalZ: Float
    ) {
        super.vertex(
            x, y, z,
            red, green, blue, alpha,
            u, v,
            overlay,
            light,
            normalX, normalY, normalZ
        )
    }
}