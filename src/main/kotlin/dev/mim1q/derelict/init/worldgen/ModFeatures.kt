package dev.mim1q.derelict.init.worldgen

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.world.feature.SpiderSilkPillar
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureConfig

object ModFeatures {
    fun init() {
        register("spider_silk_pillar", SpiderSilkPillar())
    }

    private fun <C : FeatureConfig, F : Feature<C>> register(name: String, feature: F): F {
        return Registry.register(Registries.FEATURE, Derelict.id(name), feature)
    }
}