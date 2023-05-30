package com.github.mim1q.derelict.particle.spider

import com.mojang.serialization.Codec
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction

class SpiderParticleType : ParticleType<SpiderParticleEffect>(true, SpiderParticleEffect.ParametersFactory) {
  override fun getCodec(): Codec<SpiderParticleEffect> = SpiderParticleEffect.CODEC

  fun get(direction: Direction) = SpiderParticleEffect(this, direction)

  companion object {
    fun create() = SpiderParticleType()
  }
}