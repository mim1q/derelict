package dev.mim1q.derelict.effect

import dev.mim1q.derelict.init.component.ModCardinalComponents.statusEffectComponent
import dev.mim1q.gimm1q.effect.ExtendedStatusEffect
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.registry.Registries

open class DerelictStatusEffect(
    category: StatusEffectCategory,
    color: Int,
    private val curable: Boolean = true
) : StatusEffect(category, color), ExtendedStatusEffect {
    override fun onApplied(entity: LivingEntity, amplifier: Int) {
        super.onApplied(entity, amplifier)
        entity.statusEffectComponent.addEffect(Registries.STATUS_EFFECT.getEntry(this) ?: return, amplifier)
    }

    open fun onRemoved(entity: LivingEntity, container: AttributeContainer, amplifier: Int) {
        super.onRemoved(container)
        entity.statusEffectComponent.removeEffect(Registries.STATUS_EFFECT.getEntry(this) ?: return)
    }

    override fun canBeCuredWithMilk(): Boolean = curable
}