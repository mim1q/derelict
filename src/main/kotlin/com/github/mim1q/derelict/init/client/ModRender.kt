package com.github.mim1q.derelict.init.client

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.client.render.FancyCobwebWithSpiderRenderer
import com.github.mim1q.derelict.init.ModBlockEntities
import com.github.mim1q.derelict.init.ModBlocks
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.model.EntityModelLayer

object ModRender {
  val FANCY_COBWEB_SPIDER_LAYER = registerLayer(
    FancyCobwebWithSpiderRenderer.SpiderModel::getTexturedModelData, "fancy_cobweb_spider"
  )

  fun init() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      RenderLayer.getCutout(),
      ModBlocks.BURNED_WOOD.trapdoor, ModBlocks.BURNED_WOOD.door, ModBlocks.BURNED_LEAVES, ModBlocks.SMOLDERING_LEAVES,
      ModBlocks.BURNED_GRASS.grassBlock, ModBlocks.BURNED_GRASS.grass, ModBlocks.BURNED_GRASS.tallGrass,
      ModBlocks.DRIED_GRASS.grassBlock, ModBlocks.DRIED_GRASS.grass, ModBlocks.DRIED_GRASS.tallGrass,
      ModBlocks.SMOLDERING_EMBERS, ModBlocks.SMOKING_EMBERS, ModBlocks.FLICKERING_LANTERN, ModBlocks.FLICKERING_SOUL_LANTERN,
      ModBlocks.BROKEN_LANTERN, ModBlocks.FANCY_COBWEB, ModBlocks.FANCY_COBWEB_WITH_SPIDER_NEST,
      ModBlocks.FANCY_COBWEB_WITH_SPIDER,  ModBlocks.FANCY_COBWEB_WITH_SHY_SPIDER, ModBlocks.FANCY_CORNER_COBWEB,
      ModBlocks.CORNER_COBWEB
    )

    BlockEntityRendererFactories.register(ModBlockEntities.FANCY_COBWEB_WITH_SPIDER, ::FancyCobwebWithSpiderRenderer)
  }

  fun registerLayer(provider: TexturedModelDataProvider, path: String, name: String = "main"): EntityModelLayer
    = EntityModelLayer(Derelict.id(path), name).also {
      EntityModelLayerRegistry.registerModelLayer(it, provider)
    }
}