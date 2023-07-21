package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems.ItemCategory
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import io.wispforest.owo.itemgroup.gui.ItemGroupTab
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList

object ModItemGroups {
  private val ITEM_GROUP_TEXTURE = Derelict.id("textures/gui/item_group_background.png")
  private val TAB_TEXTURE = Derelict.id("textures/gui/tabs.png")

  internal val ITEM_GROUP: OwoItemGroup = OwoItemGroup.builder(Derelict.id("derelict")) {
    ModBlocksAndItems.FLICKERING_REDSTONE_LAMP.asItem().defaultStack
  }.customTexture(ITEM_GROUP_TEXTURE).build().apply {
    tabs.add(0, ItemGroupTab(
      Icon.of(ModBlocksAndItems.FLICKERING_LANTERN),
      Text.translatable("itemGroup.derelict.derelict.tab.general"),
      supplyCategories(
        ItemCategory.GENERAL,
        ItemCategory.METAL_BLOCKS,
        ItemCategory.METAL_DECORATION,
        ItemCategory.WAXED_METAL_BLOCKS,
        ItemCategory.WAXED_METAL_DECORATION
      ),
      TAB_TEXTURE,
      true
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