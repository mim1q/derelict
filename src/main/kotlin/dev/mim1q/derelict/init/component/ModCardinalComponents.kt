package dev.mim1q.derelict.init.component

import dev.mim1q.derelict.Derelict
import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object ModCardinalComponents : EntityComponentInitializer {
    val EFFECTS_COMPONENT: ComponentKey<DerelictEffectsComponent> =
        ComponentRegistry.getOrCreate(Derelict.id("effect_flags"), DerelictEffectsComponent::class.java)

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerFor(LivingEntity::class.java, EFFECTS_COMPONENT, ::DerelictEffectsComponent)
    }

    class DerelictEffectsComponent(private val provider: Any) : Component, AutoSyncedComponent {
        private val effects = mutableMapOf<Identifier, Int>()

        override fun readFromNbt(tag: NbtCompound) {
            effects.clear()
            tag.keys.forEach { key ->
                effects[Identifier(key)] = tag.getInt(key)
            }
        }

        override fun writeToNbt(tag: NbtCompound) {
            effects.forEach { (key, value) ->
                tag.putInt(key.toString(), value)
            }
        }

        fun addEffect(effect: StatusEffect, amplifier: Int) {
            val id = Registries.STATUS_EFFECT.getId(effect) ?: return
            effects[id] = amplifier
            EFFECTS_COMPONENT.sync(provider)
        }

        fun removeEffect(effect: StatusEffect) {
            val id = Registries.STATUS_EFFECT.getId(effect) ?: return
            effects.remove(id)
            EFFECTS_COMPONENT.sync(provider)
        }

        fun getEffectAmplifier(effect: StatusEffect): Int? {
            val id = Registries.STATUS_EFFECT.getId(effect) ?: return null
            return effects[id]
        }

        fun removeAll() = effects.clear()
    }

    val LivingEntity.statusEffectComponent: DerelictEffectsComponent
        get() = EFFECTS_COMPONENT.get(this)

    fun LivingEntity.hasClientSyncedEffect(effect: StatusEffect): Boolean =
        statusEffectComponent.getEffectAmplifier(effect) != null

    fun LivingEntity.getClientSyncedEffectAmplifier(effect: StatusEffect): Int? =
        statusEffectComponent.getEffectAmplifier(effect)

    fun LivingEntity.updateClientSyncedEffects() {
        statusEffectComponent.removeAll()

        this.activeStatusEffects.forEach {
            statusEffectComponent.addEffect(it.key, it.value.amplifier)
        }
    }
}
