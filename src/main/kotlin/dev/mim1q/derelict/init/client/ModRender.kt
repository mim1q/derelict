package dev.mim1q.derelict.init.client

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.client.render.block.FancyCobwebWithSpiderRenderer
import dev.mim1q.derelict.client.render.entity.boss.arachne.ArachneEntityRenderer
import dev.mim1q.derelict.client.render.entity.boss.arachne.ArachneTexturedModelData
import dev.mim1q.derelict.init.ModBlockEntities
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import dev.mim1q.derelict.util.BlockMarkerUtils
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.model.EntityModelLayer

object ModRender {
  val FANCY_COBWEB_SPIDER_LAYER = registerLayer(FancyCobwebWithSpiderRenderer.SpiderModel::getTexturedModelData, "fancy_cobweb_spider")
  val ARACHNE_LAYER = registerLayer(ArachneTexturedModelData::create, "arachne")
  val MARKER_LAYER = registerLayer(BlockMarkerUtils::getMarkerTexturedModelData, "marker")

  fun init() {
    BlockRenderLayerMap.INSTANCE.putBlocks(
      RenderLayer.getCutout(),
      ModBlocksAndItems.BURNED_WOOD.trapdoor, ModBlocksAndItems.BURNED_WOOD.door, ModBlocksAndItems.BURNED_LEAVES, ModBlocksAndItems.SMOLDERING_LEAVES,
      ModBlocksAndItems.BURNED_GRASS.grassBlock, ModBlocksAndItems.BURNED_GRASS.grass, ModBlocksAndItems.BURNED_GRASS.tallGrass,
      ModBlocksAndItems.DRIED_GRASS.grassBlock, ModBlocksAndItems.DRIED_GRASS.grass, ModBlocksAndItems.DRIED_GRASS.tallGrass,
      ModBlocksAndItems.SMOLDERING_EMBERS, ModBlocksAndItems.SMOKING_EMBERS, ModBlocksAndItems.FLICKERING_LANTERN, ModBlocksAndItems.FLICKERING_SOUL_LANTERN,
      ModBlocksAndItems.BROKEN_LANTERN, ModBlocksAndItems.FANCY_COBWEB, ModBlocksAndItems.FANCY_COBWEB_WITH_SPIDER_NEST,
      ModBlocksAndItems.FANCY_COBWEB_WITH_SPIDER,  ModBlocksAndItems.FANCY_COBWEB_WITH_SHY_SPIDER, ModBlocksAndItems.FANCY_CORNER_COBWEB,
      ModBlocksAndItems.CORNER_COBWEB,
      *ModBlocksAndItems.NOCTISTEEL.getCutoutBlocks()
    )

    BlockEntityRendererFactories.register(ModBlockEntities.FANCY_COBWEB_WITH_SPIDER, ::FancyCobwebWithSpiderRenderer)
    EntityRendererRegistry.register(ModEntities.ARACHNE, ::ArachneEntityRenderer)
  }

  private fun registerLayer(provider: TexturedModelDataProvider, path: String, name: String = "main")
    = EntityModelLayer(Derelict.id(path), name).also {
      EntityModelLayerRegistry.registerModelLayer(it, provider)
    }
}