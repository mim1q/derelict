package dev.mim1q.derelict.effect

import dev.mim1q.derelict.init.component.ModCardinalComponents.statusEffectComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

open class DerelictStatusEffect(
    category: StatusEffectCategory,
    color: Int
) : StatusEffect(category, color) {
    override fun onApplied(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        super.onApplied(entity, attributes, amplifier)
        entity.statusEffectComponent.addEffect(this, amplifier)
    }

    override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
        super.onRemoved(entity, attributes, amplifier)
        entity.statusEffectComponent.removeEffect(this)
    }
}