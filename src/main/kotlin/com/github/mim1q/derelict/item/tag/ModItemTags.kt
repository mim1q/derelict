package com.github.mim1q.derelict.item.tag

import com.github.mim1q.derelict.Derelict
import net.minecraft.item.Item
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object ModItemTags {
  val UNWAXED_METALS = of("unwaxed_metals")
  val WAXED_METALS = of("waxed_metals")
  val GENERAL_TAB = of("general_tab")

  private fun of(id: String): TagKey<Item> = TagKey.of(Registry.ITEM_KEY, Derelict.id(id))
}