package dev.mim1q.derelict.particle.spider

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.mim1q.derelict.init.ModParticles
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleType
import net.minecraft.util.math.Direction

class SpiderParticleEffect(
    private val type: ParticleType<SpiderParticleEffect>,
    val direction: Direction,
) : ParticleEffect {
    override fun getType(): ParticleType<SpiderParticleEffect> = type

    companion object {
        val CODEC: MapCodec<SpiderParticleEffect> = RecordCodecBuilder.mapCodec {
            it.group(
                Direction.CODEC.fieldOf("direction").forGetter { it.direction }
            ).apply(it) { dir -> SpiderParticleEffect(ModParticles.SPIDER, dir) }
        }

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, SpiderParticleEffect> = PacketCodec.tuple(
            Direction.PACKET_CODEC, { it.direction },
            { direction -> SpiderParticleEffect(ModParticles.SPIDER, direction) }
        )
    }
}