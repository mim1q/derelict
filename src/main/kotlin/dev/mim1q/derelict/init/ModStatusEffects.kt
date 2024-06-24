package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModStatusEffects {
    val WEBBED = register("webbed", object : StatusEffect(StatusEffectCategory.HARMFUL, 0x000000) {
        init {
            addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "1ef41878-ab77-426c-8285-e3487da08b58", 0.0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
        }
    })

    fun init() {}

    fun <S : StatusEffect> register(name: String, effect: S): S = Registry.register(
        Registries.STATUS_EFFECT,
        Derelict.id(name),
        effect
    )
}