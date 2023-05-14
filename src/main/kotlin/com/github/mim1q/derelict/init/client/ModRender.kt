package com.github.mim1q.derelict.init.client

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.init.ModBlocks
import com.github.mim1q.derelict.init.ModItems
import com.github.mim1q.derelict.item.graffiti.SprayCanItem
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.client.render.RenderLayer

object ModRender {
  fun init() {
    BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
      ModBlocks.BURNED_WOOD.trapdoor, ModBlocks.BURNED_WOOD.door, ModBlocks.BURNED_LEAVES, ModBlocks.SMOLDERING_LEAVES
    )
    BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
      ModBlocks.SMOLDERING_EMBERS, ModBlocks.SMOKING_EMBERS
    )

    listOf(ModItems.SPRAY_CAN, ModItems.RAINBOW_SPRAY_CAN).forEach {
      ModelPredicateProviderRegistry.register(it, Derelict.id("using")) {
        stack, _, entity, _ -> if (entity != null && entity.isUsingItem && entity.activeItem == stack) 1.0F else 0.0F
      }
    }
    ColorProviderRegistry.ITEM.register(
      {stack, _ -> SprayCanItem.getColor(stack) ?: 0xFFFFFF},
      ModItems.SPRAY_CAN
    )
  }
}