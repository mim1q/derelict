package com.github.mim1q.derelict

import com.github.mim1q.derelict.init.ModParticles
import com.github.mim1q.derelict.init.client.ModRender
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader

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
  }
}