package com.github.mim1q.derelict

import com.github.mim1q.derelict.init.ModParticles
import com.github.mim1q.derelict.init.client.ModRender
import net.fabricmc.api.ClientModInitializer

object DerelictClient : ClientModInitializer {
  override fun onInitializeClient() {
    ModRender.init()
    ModParticles.initClient()
  }
}