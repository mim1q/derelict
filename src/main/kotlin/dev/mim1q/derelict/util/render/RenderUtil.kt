package dev.mim1q.derelict.util.render

import net.minecraft.client.util.math.MatrixStack

inline fun MatrixStack.entry(setup: MatrixStack.() -> Unit) {
    push()
    setup()
    pop()
}