package dev.mim1q.derelict.particle.colored

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType


class ColoredParticleType private constructor() :
    ParticleType<ColoredParticleEffect>(true) {

    override fun getCodec(): MapCodec<ColoredParticleEffect> =
        ColoredParticleEffect.createMapCodec(this)

    override fun getPacketCodec(): PacketCodec<RegistryByteBuf, ColoredParticleEffect> =
        ColoredParticleEffect.createPacketCodec(this)

    fun get(color: Int): ParticleEffect = ColoredParticleEffect(this, color)

    companion object {
        fun create(): ColoredParticleType {
            return ColoredParticleType()
        }
    }
}