package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.init.ModBlocksAndItems.ItemCategory
import com.github.mim1q.derelict.item.tag.ModItemTags
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import io.wispforest.owo.itemgroup.gui.ItemGroupTab
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList

object ModItemGroups {
  private val ITEM_GROUP_TEXTURE = Derelict.id("textures/gui/derelictitemtab.png")

  internal val ITEM_GROUP: OwoItemGroup = OwoItemGroup.builder(Derelict.id("derelict")) {
    ModBlocksAndItems.FLICKERING_REDSTONE_LAMP.asItem().defaultStack
  }.customTexture(ITEM_GROUP_TEXTURE).build().apply {
    addTab(Icon.of(ModBlocksAndItems.FLICKERING_LANTERN), "general", ModItemTags.GENERAL_TAB, false)
    tabs.add(1, ItemGroupTab(
      Icon.of(ModBlocksAndItems.CONSTRUCTION_STEEL.unaffected.block.asItem().defaultStack),
      Text.translatable("itemGroup.derelict.derelict.tab.metals"),
      supplyCategories(ItemCategory.METAL_BLOCKS, ItemCategory.METAL_DECORATION, ItemCategory.WAXED_METAL_BLOCKS, ItemCategory.WAXED_METAL_DECORATION),
      ItemGroupTab.DEFAULT_TEXTURE,
      false
    ))
    initialize()
  }

  private fun supplyCategories(vararg categories: ItemCategory): (DefaultedList<ItemStack>) -> Unit {
    return { stacks ->
      stacks.addAll(
        categories
          .map { category -> category.items.map { item -> item.defaultStack } }
          .flatten()
      )
    }
  }
}