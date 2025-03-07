package dev.mim1q.derelict.init.worldgen

import com.mojang.datafixers.util.Pair
import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeKeys
import net.minecraft.world.biome.SpawnSettings
import net.minecraft.world.biome.source.util.MultiNoiseUtil
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube
import net.minecraft.world.biome.source.util.MultiNoiseUtil.ParameterRange
import net.minecraft.world.gen.surfacebuilder.MaterialRules
import net.minecraft.world.gen.surfacebuilder.MaterialRules.block
import net.minecraft.world.gen.surfacebuilder.MaterialRules.condition
import terrablender.api.*
import terrablender.api.ParameterUtils.*
import java.util.function.Consumer

object ModBiomes : TerraBlenderApi {
    private val SPIDER_CAVES = biomeKey("spider_caves")
    private val DERELICT_REGION = object : Region(Derelict.id("derelict"), RegionType.OVERWORLD, 1) {
        override fun addBiomes(
            registry: Registry<Biome>,
            mapper: Consumer<Pair<NoiseHypercube, RegistryKey<Biome>>>
        ) {

            val spiderCavesPoints = ParameterPointListBuilder()
                .temperature(Temperature.FULL_RANGE)
                .humidity(Humidity.WET, Humidity.HUMID, Humidity.ARID)
                .continentalness(Continentalness.FULL_RANGE)
                .erosion(Erosion.FULL_RANGE)
                .depth(Depth.UNDERGROUND)
                .weirdness(Weirdness.FULL_RANGE)
                .offset(0.38f)
                .build()

            addModifiedVanillaOverworldBiomes(mapper) {
                spiderCavesPoints.forEach { addBiome(mapper, it, SPIDER_CAVES) }
            }
        }
    }

    override fun onTerraBlenderInitialized() {
        Regions.register(DERELICT_REGION)

        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            Derelict.MOD_ID,
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
                            ModEntities.JUMPING_SPIDER, 30, 1, 2
                        )
                    )
                }
            )

        BiomeModifications.create(Derelict.id("add_spiny_spiders"))
            .add(
                ModificationPhase.ADDITIONS,
                { selection -> selection.biomeKey == BiomeKeys.CRIMSON_FOREST },
                { _, modification ->
                    modification.spawnSettings.addSpawn(
                        SpawnGroup.MONSTER,
                        SpawnSettings.SpawnEntry(
                            ModEntities.SPINY_SPIDER, 15, 1, 2
                        )
                    )
                }
            )

        BiomeModifications.create(Derelict.id("add_charming_spiders"))
            .add(
                ModificationPhase.ADDITIONS,
                { selection -> selection.biomeKey == BiomeKeys.WARPED_FOREST },
                { _, modification ->
                    modification.spawnSettings.addSpawn(
                        SpawnGroup.MONSTER,
                        SpawnSettings.SpawnEntry(
                            ModEntities.CHARMING_SPIDER, 15, 1, 2
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

    private fun biomeKey(name: String): RegistryKey<Biome> = RegistryKey.of(RegistryKeys.BIOME, Derelict.id(name))
}