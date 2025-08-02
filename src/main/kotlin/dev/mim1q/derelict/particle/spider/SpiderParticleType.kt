package dev.mim1q.derelict.particle.spider

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction

class SpiderParticleType : ParticleType<SpiderParticleEffect>(true) {
    override fun getPacketCodec(): PacketCodec<in RegistryByteBuf, SpiderParticleEffect> =
        SpiderParticleEffect.PACKET_CODEC

    override fun getCodec(): MapCodec<SpiderParticleEffect> = SpiderParticleEffect.CODEC

    fun get(direction: Direction) = SpiderParticleEffect(this, direction)

    companion object {
        fun create() = SpiderParticleType()
    }
}