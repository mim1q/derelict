package dev.mim1q.derelict.init.worldgen

import com.terraformersmc.biolith.api.biome.BiomePlacement
import com.terraformersmc.biolith.api.surface.SurfaceGeneration
import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.SpawnSettings
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange
import net.minecraft.world.gen.surfacebuilder.MaterialRules
import net.minecraft.world.gen.surfacebuilder.MaterialRules.block
import net.minecraft.world.gen.surfacebuilder.MaterialRules.condition

object ModBiomes {
    val SPIDER_CAVES = biomeKey("spider_caves")

    fun init() {
        BiomePlacement.addOverworld(
            SPIDER_CAVES,
            noiseParameters(
                depth = 1.2f..2.0f,
                weirdness = 0.72f..0.77f,
                offset = 0.2f
            )
        )
        BiomePlacement.addOverworld(
            SPIDER_CAVES,
            noiseParameters(
                continentalness = -0.1f..1.0f,
                depth = 0.0f..1.2f,
                weirdness = 0.86f..1.0f,
                offset = 0.3f
            )
        )

        SurfaceGeneration.addOverworldSurfaceRules(
            Derelict.id("rules/overworld/spider_caves"),
            condition(
                MaterialRules.biome(SPIDER_CAVES),
                block(ModBlocksAndItems.ARACHNITE.block.defaultState)
            )
        )

        BiomeModifications.create(Derelict.id("add_jumping_spiders"))
            .add(
                ModificationPhase.ADDITIONS,
                { selection ->
                    selection.biome.spawnSettings.getSpawnEntries(SpawnGroup.MONSTER).entries
                        .any { it.type == EntityType.SPIDER }
                },
                { _, modification ->
                    modification.spawnSettings.addSpawn(
                        SpawnGroup.MONSTER,
                        SpawnSettings.SpawnEntry(
                            ModEntities.JUMPING_SPIDER, 80, 1, 2
                        )
                    )
                }
            )
    }

    private fun noiseParameters(
        temperature: ClosedFloatingPointRange<Float> = -1f..1f,
        humidity: ClosedFloatingPointRange<Float> = -1f..1f,
        continentalness: ClosedFloatingPointRange<Float> = -1f..1f,
        erosion: ClosedFloatingPointRange<Float> = -1f..1f,
        depth: ClosedFloatingPointRange<Float> = -1f..1f,
        weirdness: ClosedFloatingPointRange<Float> = -1f..1f,
        offset: Float = 0f
    ) = NoiseHypercube(
        ParameterRange.of(temperature.start, temperature.endInclusive),
        ParameterRange.of(humidity.start, humidity.endInclusive),
        ParameterRange.of(continentalness.start, continentalness.endInclusive),
        ParameterRange.of(erosion.start, erosion.endInclusive),
        ParameterRange.of(depth.start, depth.endInclusive),
        ParameterRange.of(weirdness.start, weirdness.endInclusive),
        MultiNoiseUtil.toLong(offset)
    )

    private fun biomeKey(name: String) = RegistryKey.of(RegistryKeys.BIOME, Derelict.id(name))
}