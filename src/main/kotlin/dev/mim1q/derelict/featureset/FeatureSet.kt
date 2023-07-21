package dev.mim1q.derelict.featureset

import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModBlocksAndItems.ItemCategory
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.util.Identifier

abstract class FeatureSet(
  id: Identifier,
  protected val defaultItemSettings: FabricItemSettings = FabricItemSettings(),
  protected val defaultBlockSettings: FabricBlockSettings = FabricBlockSettings.copyOf(Blocks.STONE)
) {
  protected val name = id.path
  protected val namespace = id.namespace

  protected fun id(name: String) = Identifier(namespace, name)
  open fun register(): FeatureSet = this

  protected fun <I : Item> registerItem(
    name: String,
    item: I,
    category: ItemCategory = ItemCategory.GENERAL
  ): I = ModBlocksAndItems.registerItem(name, item, category)

  protected fun <B : Block> registerBlock(name: String, block: B): B = ModBlocksAndItems.registerBlock(name, block)

  protected fun <B : Block> registerBlockWithItem(
    name: String,
    block: B,
    category: ItemCategory = ItemCategory.GENERAL
  ): B = ModBlocksAndItems.register(name, block, category)
}