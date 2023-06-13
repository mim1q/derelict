package com.github.mim1q.derelict.util

import com.github.mim1q.derelict.Derelict
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier

object RenderUtil {
  private val HONEYCOMB_TEXTURE = Identifier("textures/item/honeycomb.png")

  fun renderWaxedIndicator(x: Double, y: Double) {
    val size = Derelict.CLIENT_CONFIG.waxedIndicatorScale() * 16
    val alpha = Derelict.CLIENT_CONFIG.waxedIndicatorOpacity()
    val tessellator = Tessellator.getInstance()
    val bufferBuilder = tessellator.buffer
    RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
    RenderSystem.setShaderTexture(0, HONEYCOMB_TEXTURE)
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha)
    RenderSystem.enableBlend()
    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR)
    val z = 300.0
    bufferBuilder.vertex(x, y + size, z).texture(0f, 1f).color(255, 255, 255, 255).next()
    bufferBuilder.vertex(x + size, y + size, z).texture(1f, 1f).color(255, 255, 255, 255).next()
    bufferBuilder.vertex(x + size, y, z).texture(1f, 0f).color(255, 255, 255, 255).next()
    bufferBuilder.vertex(x, y, z).texture(0f, 0f).color(255, 255, 255, 255).next()
    tessellator.draw()
    RenderSystem.disableBlend()
  }
}