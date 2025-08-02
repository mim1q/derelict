package dev.mim1q.derelict.featureset

import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModBlocksAndItems.ItemCategory
import dev.mim1q.derelict.util.extensions.copy
import net.minecraft.block.AbstractBlock
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.util.Identifier

abstract class FeatureSet(
    id: Identifier,
    protected val defaultItemSettings: Item.Settings = Item.Settings(),
    private val defaultBlockSettings: Settings = Settings.copy(Blocks.STONE)
) {
    protected val name = id.path
    protected val namespace = id.namespace

    protected fun id(name: String) = Identifier.of(namespace, name)
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

    protected fun defaultBlockSettings(): AbstractBlock.Settings = defaultBlockSettings.copy()
}