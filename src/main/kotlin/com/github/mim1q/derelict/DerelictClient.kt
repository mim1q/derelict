package com.github.mim1q.derelict

import com.github.mim1q.derelict.init.ModParticles
import com.github.mim1q.derelict.init.client.ModRender
import com.github.mim1q.derelict.item.CrosshairTipItem
import com.github.mim1q.derelict.util.RenderUtil
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items

object DerelictClient : ClientModInitializer {
  override fun onInitializeClient() {
    ModRender.init()
    ModParticles.initClient()

    ResourceManagerHelper.registerBuiltinResourcePack(
      Derelict.id("arachnophobia_filter"),
      FabricLoader.getInstance().getModContainer("derelict").get(),
      "Arachnophobia Filter",
      ResourcePackActivationType.NORMAL,
    )

    HudRenderCallback.EVENT.register { _, _ ->
      MinecraftClient.getInstance().player?.let {player ->
        listOf(player.mainHandStack.item, player.offHandStack.item)
      }?.forEach { item ->
        if (item != null && item is CrosshairTipItem || item == Items.HONEYCOMB) {
          RenderUtil.renderCrosshairTip(item as CrosshairTipItem)
          return@register
        }
      }
    }
  }
}