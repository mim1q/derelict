package dev.mim1q.derelict.init.worldgen

import com.mojang.serialization.Codec
import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.world.feature.JigsawFeature
import dev.mim1q.derelict.world.feature.LocalGravityProcessor
import dev.mim1q.derelict.world.feature.SpiderSilkParabola
import dev.mim1q.derelict.world.feature.SpiderSilkPillar
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig

object ModFeatures {
    val LOCAL_GRAVITY_PROCESSOR = registerProcessor("local_gravity", LocalGravityProcessor.CODEC)

    fun init() {
        register("spider_silk_pillar", SpiderSilkPillar())
        register("spider_silk_parabola", SpiderSilkParabola())
        register("jigsaw_feature", JigsawFeature())
    }

    private fun <C : FeatureConfig, F : Feature<C>> register(name: String, feature: F): F {
        return Registry.register(Registries.FEATURE, Derelict.id(name), feature)
    }

    private fun <P : StructureProcessor> registerProcessor(id: String, codec: Codec<P>): StructureProcessorType<P> =
        Registry.register(
            Registries.STRUCTURE_PROCESSOR,
            Derelict.id(id),
            StructureProcessorType { codec }
        )

}