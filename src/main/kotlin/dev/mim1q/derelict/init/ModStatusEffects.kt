package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.effect.DerelictStatusEffect
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

object ModStatusEffects {
    val COBWEBBED = register("cobwebbed", object : DerelictStatusEffect(StatusEffectCategory.HARMFUL, 0x000000, false) {
        override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean = duration == 5 && amplifier > 0

        override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
            entity.removeStatusEffectInternal(this)
            entity.addStatusEffect(
                StatusEffectInstance(
                    this,
                    if (entity.isPlayer) 60 else 40,
                    amplifier - 1,
                    false,
                    false,
                    true
                )
            )
            spawnParticlesAndPlaySound(entity)
        }

        override fun onRemoved(entity: LivingEntity, attributes: AttributeContainer, amplifier: Int) {
            super.onRemoved(entity, attributes, amplifier)
            spawnParticlesAndPlaySound(entity)
        }

        private fun spawnParticlesAndPlaySound(entity: LivingEntity) {
            (entity.world as? ServerWorld)?.let {
                it.spawnParticles(
                    BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COBWEB.defaultState),
                    entity.x,
                    entity.y + entity.height / 2.0,
                    entity.z,
                    20,
                    entity.width / 3.0,
                    entity.height / 3.0,
                    entity.width / 3.0,
                    0.0
                )

                it.playSound(null, entity.blockPos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1.0f, 1.2f)
            }
        }
    })

    fun init() {}

    fun <S : StatusEffect> register(name: String, effect: S): S = Registry.register(
        Registries.STATUS_EFFECT,
        Derelict.id(name),
        effect
    )
}