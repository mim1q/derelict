package com.github.mim1q.derelict.init.client

import com.github.mim1q.derelict.init.ModBlocks
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

object ModRender {
  fun init() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      RenderLayer.getCutout(),
      ModBlocks.BURNED_WOOD.trapdoor, ModBlocks.BURNED_WOOD.door, ModBlocks.BURNED_LEAVES, ModBlocks.SMOLDERING_LEAVES,
      ModBlocks.BURNED_GRASS.grassBlock, ModBlocks.BURNED_GRASS.grass, ModBlocks.BURNED_GRASS.tallGrass,
      ModBlocks.DRIED_GRASS.grassBlock, ModBlocks.DRIED_GRASS.grass, ModBlocks.DRIED_GRASS.tallGrass,
      ModBlocks.SMOLDERING_EMBERS, ModBlocks.SMOKING_EMBERS, ModBlocks.FLICKERING_LANTERN, ModBlocks.FLICKERING_SOUL_LANTERN,
      ModBlocks.BROKEN_LANTERN, ModBlocks.FANCY_COBWEB
    )
  }
}