package dev.mim1q.derelict.entity.damage

import dev.mim1q.derelict.Derelict.id
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.World

class DerelictDamageSource private constructor(val key: RegistryKey<DamageType>) {
    fun get(world: World, entity: Entity?, indirectEntity: Entity?): DamageSource {
        return DamageSource(
            world.registryManager.get(RegistryKeys.DAMAGE_TYPE).entryOf(key),
            entity,
            indirectEntity
        )
    }

    fun get(world: World, entity: Entity?): DamageSource {
        return DamageSource(world.registryManager.get(RegistryKeys.DAMAGE_TYPE).entryOf(key), entity)
    }

    fun get(world: World): DamageSource {
        return get(world, null)
    }

    companion object {
        val SPIDERLING_ALLY: DerelictDamageSource = create("spiderling_ally")

        private fun create(name: String): DerelictDamageSource {
            return DerelictDamageSource(RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id(name)))
        }
    }
}