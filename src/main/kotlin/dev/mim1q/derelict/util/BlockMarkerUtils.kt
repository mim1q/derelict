package dev.mim1q.derelict.util

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.client.render.block.entry
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.interfaces.AbstractBlockAccessor
import dev.mim1q.derelict.item.CrosshairTipItem
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.model.*
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import kotlin.math.sin


object BlockMarkerUtils {
  private val HONEYCOMB_TEXTURE = Identifier("textures/item/honeycomb.png")

  fun renderWaxedIndicator(textureDrawer: (Identifier, Int, Int, Int, Int, Float) -> Unit, x: Int, y: Int) {
    val size = (Derelict.CLIENT_CONFIG.waxedIndicatorScale() * 16).toInt()
    val startX = x - size / 2
    val startY = y - size / 2
    val alpha = Derelict.CLIENT_CONFIG.waxedIndicatorOpacity()
    textureDrawer(HONEYCOMB_TEXTURE, startX, startY, 300, size, alpha)
  }

  fun renderCrosshairTip(context: DrawContext, item: CrosshairTipItem) {
    val target = MinecraftClient.getInstance().crosshairTarget
    if (target?.type == HitResult.Type.BLOCK) {
      val block = MinecraftClient.getInstance().world?.getBlockState((target as BlockHitResult).blockPos)?.block
      if (item.shouldShowTip(block)) {
        renderCrosshairTipTexture(context, item.getTipTexture())
      }
    }
  }

  private fun renderCrosshairTipTexture(context: DrawContext, texture: Identifier) {
//    RenderSystem.setShader(GameRenderer::getPositionTexProgram)
    val x = MinecraftClient.getInstance().window.scaledWidth / 2 + 6
    val y = MinecraftClient.getInstance().window.scaledHeight / 2 - 8

    context.drawTexture(texture, x, y, 0F, 0F, 16, 16, 16, 16)
  }
}