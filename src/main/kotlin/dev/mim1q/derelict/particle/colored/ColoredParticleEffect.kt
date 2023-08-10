package dev.mim1q.derelict.particle.colored

import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries


@JvmRecord
data class ColoredParticleEffect(val type: ParticleType<*>, val color: Int) : ParticleEffect {
  override fun getType() = type

  override fun write(buf: PacketByteBuf) {
    buf.writeInt(color)
  }

  override fun asString(): String = "${Registries.PARTICLE_TYPE.getId(type)}$color"

  companion object {
    @Suppress("DEPRECATION")
    val PARAMETERS_FACTORY: ParticleEffect.Factory<ColoredParticleEffect> =
      object : ParticleEffect.Factory<ColoredParticleEffect> {
        override fun read(
          particleType: ParticleType<ColoredParticleEffect>,
          stringReader: StringReader
        ): ColoredParticleEffect {
          stringReader.expect(' ')
          return ColoredParticleEffect(particleType, stringReader.readInt())
        }

        override fun read(
          particleType: ParticleType<ColoredParticleEffect>,
          packetByteBuf: PacketByteBuf
        ): ColoredParticleEffect {
          return ColoredParticleEffect(particleType, packetByteBuf.readInt())
        }
      }

    fun createCodec(type: ParticleType<ColoredParticleEffect>): Codec<ColoredParticleEffect> {
      return Codec.INT.xmap(
        { color: Int ->
          ColoredParticleEffect(
            type,
            color
          )
        }
      ) { (_, color): ColoredParticleEffect -> color }
    }
  }
}