package dev.mim1q.derelict.init.component

import dev.mim1q.derelict.Derelict
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import org.ladysnake.cca.api.v3.component.Component
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistry
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer

object ModCardinalComponents : EntityComponentInitializer {
    val EFFECTS_COMPONENT: ComponentKey<DerelictEffectsComponent> =
        ComponentRegistry.getOrCreate(Derelict.id("effect_flags"), DerelictEffectsComponent::class.java)

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerFor(LivingEntity::class.java, EFFECTS_COMPONENT, ::DerelictEffectsComponent)
    }

    class DerelictEffectsComponent(private val provider: Any) : Component, AutoSyncedComponent {
        private val effects = mutableMapOf<Identifier, Int>()


        override fun readFromNbt(tag: NbtCompound, registry: WrapperLookup) {
            effects.clear()
            tag.keys.forEach { key ->
                effects[Identifier.of(key)] = tag.getInt(key)
            }
        }

        override fun writeToNbt(tag: NbtCompound, registry: WrapperLookup) {
            effects.forEach { (key, value) ->
                tag.putInt(key.toString(), value)
            }
        }

        fun addEffect(effect: RegistryEntry<StatusEffect>, amplifier: Int) {
            val id = effect.key.get().value
            effects[id] = amplifier
            EFFECTS_COMPONENT.sync(provider)
        }

        fun removeEffect(effect: RegistryEntry<StatusEffect>) {
            val id = effect.key.get().value
            effects.remove(id)
            EFFECTS_COMPONENT.sync(provider)
        }

        fun getEffectAmplifier(effect: RegistryEntry<StatusEffect>): Int? {
            val id = effect.key.get().value
            return effects[id]
        }

        fun removeAll() = effects.clear()
    }

    val LivingEntity.statusEffectComponent: DerelictEffectsComponent
        get() = EFFECTS_COMPONENT.get(this)

    fun LivingEntity.hasClientSyncedEffect(effect: RegistryEntry<StatusEffect>): Boolean =
        statusEffectComponent.getEffectAmplifier(effect) != null

    fun LivingEntity.getClientSyncedEffectAmplifier(effect: RegistryEntry<StatusEffect>): Int? =
        statusEffectComponent.getEffectAmplifier(effect)

    fun LivingEntity.updateClientSyncedEffects() {
        statusEffectComponent.removeAll()

        this.activeStatusEffects.forEach {
            statusEffectComponent.addEffect(it.key, it.value.amplifier)
        }
    }
}
