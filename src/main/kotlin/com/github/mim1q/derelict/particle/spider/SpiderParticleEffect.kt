package com.github.mim1q.derelict.particle.spider

import com.github.mim1q.derelict.init.ModParticles
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry

class SpiderParticleEffect(
  private val type: ParticleType<SpiderParticleEffect>,
  val direction: Direction
) : ParticleEffect {
  override fun getType(): ParticleType<SpiderParticleEffect> = type

  override fun write(buf: PacketByteBuf) {
    buf.writeInt(direction.id)
  }

  override fun asString(): String = "${Registry.PARTICLE_TYPE.getId(type)}$direction"

  @Suppress("DEPRECATION")
  object ParametersFactory : ParticleEffect.Factory<SpiderParticleEffect> {
    override fun read(
      particleType: ParticleType<SpiderParticleEffect>,
      stringReader: StringReader
    ): SpiderParticleEffect {
      stringReader.expect(' ')
      return SpiderParticleEffect(particleType, Direction.byId(stringReader.readInt()))
    }

    override fun read(
      particleType: ParticleType<SpiderParticleEffect>,
      packetByteBuf: PacketByteBuf
    ): SpiderParticleEffect {
      return SpiderParticleEffect(particleType, Direction.byId(packetByteBuf.readInt()))
    }
  }

  companion object {
    val CODEC = Codec.INT.xmap(
      { direction: Int -> SpiderParticleEffect(ModParticles.SPIDER, Direction.byId(direction)) }
    ) { it.direction.id }
  }
}