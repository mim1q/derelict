package dev.mim1q.derelict.world.feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.structure.pool.StructurePoolBasedGenerator
import net.minecraft.util.Identifier
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.ChunkRandom
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext
import net.minecraft.world.gen.structure.Structure
import java.util.*

class JigsawFeature :
    Feature<JigsawFeature.JigsawFeatureConfig>(JigsawFeatureConfig.CODEC) {
    override fun generate(context: FeatureContext<JigsawFeatureConfig>): Boolean {
        val world = context.world
        val registryManager = world.registryManager
        val noiseConfig =
            if (world.chunkManager is ServerChunkManager) (world.chunkManager as ServerChunkManager).noiseConfig else null
        val poolRegistry = registryManager.get(RegistryKeys.TEMPLATE_POOL)
        val optPoolEntry = poolRegistry.getEntry(RegistryKey.of(RegistryKeys.TEMPLATE_POOL, context.config.templatePool))
        if (optPoolEntry.isEmpty) return false

        val pos = context.origin.add(this.getOffset(context.origin))
        return StructurePoolBasedGenerator.generate(
            Structure.Context(
                registryManager,
                context.generator,
                context.generator.biomeSource,
                noiseConfig,
                (world.server ?: return false).structureTemplateManager,
                ChunkRandom(context.random),
                world.seed,
                ChunkPos(pos),
                world,
            ) { true },
            optPoolEntry.get(),
            Optional.empty(),
            8,
            pos,
            false,
            Optional.empty(),
            16
        ).isPresent
    }

    private fun getOffset(start: Vec3i): Vec3i {
        return Vec3i.ZERO
    }

    data class JigsawFeatureConfig(
        val templatePool: Identifier,
        val start: Identifier
    ) : FeatureConfig {
        companion object {
            val CODEC: Codec<JigsawFeatureConfig> =
                RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<JigsawFeatureConfig> ->
                    instance.group(
                        Identifier.CODEC.fieldOf("template_pool").forGetter(JigsawFeatureConfig::templatePool),
                        Identifier.CODEC.fieldOf("start").forGetter(JigsawFeatureConfig::start)
                    ).apply(instance, ::JigsawFeatureConfig)
                }
        }
    }
}