package dev.mim1q.derelict.particle.colored

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType


@JvmRecord
data class ColoredParticleEffect(val type: ParticleType<*>, val color: Int) : ParticleEffect {
    override fun getType() = type

    companion object {
        fun createMapCodec(type: ParticleType<ColoredParticleEffect>): MapCodec<ColoredParticleEffect> =
            RecordCodecBuilder.mapCodec { it ->
                it.group(
                    Codec.INT.fieldOf("color").forGetter { o -> o.color }
                ).apply(it) { color -> ColoredParticleEffect(type, color) }
            }

        fun createPacketCodec(type: ParticleType<ColoredParticleEffect>): PacketCodec<RegistryByteBuf, ColoredParticleEffect> =
            PacketCodec.tuple(
                PacketCodecs.INTEGER, { it.color },
                { color -> ColoredParticleEffect(type, color) }
            )
    }
}