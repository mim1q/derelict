package dev.mim1q.derelict.tag

import dev.mim1q.derelict.Derelict
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModEntityTags {
    val SPAWNS_SPIDERLINGS_ON_DEATH = of("spawns_spiderlings_on_death")

    private fun of(id: String): TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, Derelict.id(id))
}