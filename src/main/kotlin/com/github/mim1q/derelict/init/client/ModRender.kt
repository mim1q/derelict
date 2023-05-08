package com.github.mim1q.derelict.init.client

import com.github.mim1q.derelict.init.ModBlocks
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

object ModRender {
  fun init() {
    initBlocks()
  }

  private fun initBlocks() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      RenderLayer.getCutout(),
      ModBlocks.BURNED_WOOD.trapdoor, ModBlocks.BURNED_WOOD.door,
    )
  }
}