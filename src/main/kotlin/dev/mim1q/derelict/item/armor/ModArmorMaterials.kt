@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package dev.mim1q.derelict.item.armor

import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvents
import java.util.*
import java.util.Map

object ModArmorMaterials {
    val SPIDER: RegistryEntry<ArmorMaterial> = ArmorMaterials.register(
        "derelict_spider",
        EnumMap(
            Map.of(
                ArmorItem.Type.HELMET, 13,
                ArmorItem.Type.CHESTPLATE, 15,
                ArmorItem.Type.LEGGINGS, 16,
                ArmorItem.Type.BOOTS, 11
            )
        ),
        1,
        Registries.SOUND_EVENT.getEntry(SoundEvents.ENTITY_SPIDER_STEP),
        0f,
        0.0f,
        { Ingredient.ofItems(Items.WHITE_WOOL) },
        listOf()
    )
}
