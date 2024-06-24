package dev.mim1q.derelict.init.component

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.effect.DerelictStatusEffect
import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound

object ModCardinalComponents : EntityComponentInitializer {
    val EFFECT_FLAGS: ComponentKey<EffectFlagsComponent> =
        ComponentRegistry.getOrCreate(Derelict.id("effect_flags"), EffectFlagsComponent::class.java)

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerFor(LivingEntity::class.java, EFFECT_FLAGS, ::EffectFlagsComponent)
    }

    class EffectFlagsComponent(private val provider: Any) : Component, AutoSyncedComponent {
        private var flags: Long = 0L

        override fun readFromNbt(tag: NbtCompound) {
            flags = tag.getLong("flags")
        }

        override fun writeToNbt(tag: NbtCompound) {
            tag.putLong("flags", flags)
        }

        fun setFlag(flag: Int, value: Boolean) {
            flags = if (value) {
                flags or (1L shl flag)
            } else {
                flags and (1L shl flag).inv()
            }
            EFFECT_FLAGS.sync(provider)
        }

        fun getFlag(flag: Int): Boolean {
            return flags and (1L shl flag) != 0L
        }
    }
}

fun LivingEntity.setStatusEffectFlag(effect: DerelictStatusEffect, value: Boolean) {
    val flags = ModCardinalComponents.EFFECT_FLAGS[this]
    flags.setFlag(effect.flagOffset, value)
}

fun LivingEntity.hasDerelictStatusEffect(effect: DerelictStatusEffect): Boolean =
    ModCardinalComponents.EFFECT_FLAGS.maybeGet(this).map { it.getFlag(effect.flagOffset) }.orElse(false)
