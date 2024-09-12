package dev.mim1q.derelict.world.feature

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.mim1q.derelict.other.MineCellsStructurePoolBasedGenerator
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3i
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig
import net.minecraft.world.gen.feature.util.FeatureContext

class JigsawFeature :
    Feature<JigsawFeature.JigsawFeatureConfig>(JigsawFeatureConfig.CODEC) {
    override fun generate(context: FeatureContext<JigsawFeatureConfig>): Boolean {
        val world = context.world
        val registryManager = world.registryManager
        val noiseConfig =
            if (world.chunkManager is ServerChunkManager) (world.chunkManager as ServerChunkManager).noiseConfig else null
        val poolRegistry = registryManager.get(RegistryKeys.TEMPLATE_POOL)
        val optPoolEntry = poolRegistry.getEntry(RegistryKey.of(RegistryKeys.TEMPLATE_POOL, context.config.templatePool))
        if (optPoolEntry.isEmpty) {
            return false
        }
        val pos = context.origin.add(this.getOffset(context.origin))
        val rotation = BlockRotation.random(context.random)
        MineCellsStructurePoolBasedGenerator.generate(
            world,
            context.generator,
            registryManager,
            world.toServerWorld().structureTemplateManager,
            world.toServerWorld().structureAccessor,
            noiseConfig,
            context.random,
            world.seed + context.origin.hashCode(),
            optPoolEntry.get(),
            8,
            pos,
            rotation
        )
        return true
    }

    private fun getOffset(start: Vec3i): Vec3i {
        return Vec3i(0, 1, 0)
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