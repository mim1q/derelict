package dev.mim1q.derelict.particle.colored

import com.mojang.serialization.Codec
import net.minecraft.particle.ParticleEffect

import net.minecraft.particle.ParticleType


class ColoredParticleType private constructor() :
  ParticleType<ColoredParticleEffect>(true, ColoredParticleEffect.PARAMETERS_FACTORY) {
  override fun getCodec(): Codec<ColoredParticleEffect> = ColoredParticleEffect.createCodec(this)

  fun get(color: Int): ParticleEffect = ColoredParticleEffect(this, color)

  companion object {
    fun create(): ColoredParticleType {
      return ColoredParticleType()
    }
  }
}