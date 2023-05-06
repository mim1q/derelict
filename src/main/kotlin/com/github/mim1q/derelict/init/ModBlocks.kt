package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.family.BlockFamily
import com.github.mim1q.derelict.family.WoodBlockFamily
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry

object ModBlocks {
  val TEST_WOOD = register(WoodBlockFamily("test"))

  fun init() { }
  private fun <T : Block> register(name: String, block: T): T {
    Registry.register(Registry.BLOCK, Derelict.id(name), block)
    Registry.register(Registry.ITEM, Derelict.id(name), BlockItem(block, FabricItemSettings().group(Derelict.ITEM_GROUP)))
    return block
  }
  private fun <T : BlockFamily> register(family: T): T {
    family.getBlocks().forEach { (k, v) -> register(k, v) }
    return family
  }
}