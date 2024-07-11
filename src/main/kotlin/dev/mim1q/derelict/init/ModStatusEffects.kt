package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.effect.DerelictStatusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModStatusEffects {
    val COBWEBBED = register("cobwebbed", DerelictStatusEffect(StatusEffectCategory.HARMFUL, 0x000000))

    fun init() {}

    fun <S : StatusEffect> register(name: String, effect: S): S = Registry.register(
        Registries.STATUS_EFFECT,
        Derelict.id(name),
        effect
    )
}