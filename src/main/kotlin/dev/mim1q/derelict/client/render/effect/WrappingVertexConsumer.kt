package dev.mim1q.derelict.client.render.effect

import net.minecraft.client.render.VertexConsumer

class WrappingVertexConsumer(
    private val inner: VertexConsumer,
    val offset: Float = 0.25f,
    val skipPlanes: Boolean = false,
) : VertexConsumer by inner