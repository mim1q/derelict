package dev.mim1q.derelict.item.tag

import dev.mim1q.derelict.Derelict
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModItemTags {
    val UNWAXED_METALS = of("unwaxed_metals")
    val WAXED_METALS = of("waxed_metals")
    val GENERAL_TAB = of("general_tab")

    val WAXED_COMMON = ofCommon("waxed")

    private fun of(id: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Derelict.id(id))
    private fun ofCommon(id: String): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier("c", id))
}