package com.github.mim1q.derelict.featureset

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

abstract class FeatureSet(
  id: Identifier,
  protected val defaultItemSettings: FabricItemSettings = FabricItemSettings(),
  protected val defaultBlockSettings: FabricBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE)
) {
  protected val name = id.path
  protected val namespace = id.namespace

  protected fun id(name: String) = Identifier(namespace, name)
  open fun register(): FeatureSet = this

  protected fun <I : Item> registerItem(name: String, item: I): I = Registry.register(Registry.ITEM, id(name), item)

  protected fun <B : Block> registerBlock(name: String, block: B): B = Registry.register(Registry.BLOCK, id(name), block)

  protected fun <B : Block> registerBlockWithItem(
    name: String,
    block: B,
    settings: FabricItemSettings = defaultItemSettings,
    item: Item? = BlockItem(block, settings)
  ): B = registerBlock(name, block).also { if (item != null) registerItem(name, item) }
}