package com.github.mim1q.derelict.util

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.item.CrosshairTipItem
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.Block
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult

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

  fun renderCrosshairTip(item: CrosshairTipItem) {
    val target = MinecraftClient.getInstance().crosshairTarget
    if (target?.type == HitResult.Type.BLOCK) {
      val block = MinecraftClient.getInstance().world?.getBlockState((target as BlockHitResult).blockPos)?.block
      if (item.shouldShowTip(block)) {
        renderCrosshairTipTexture(item.getTipTexture())
      }
    }
  }

  private fun renderCrosshairTipTexture(texture: Identifier) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader)
    RenderSystem.setShaderTexture(0, texture)
    val x = MinecraftClient.getInstance().window.scaledWidth / 2 + 6
    val y = MinecraftClient.getInstance().window.scaledHeight / 2 - 8
    DrawableHelper.drawTexture(MatrixStack(), x, y, 0F, 0F, 16, 16, 16, 16)
  }
}