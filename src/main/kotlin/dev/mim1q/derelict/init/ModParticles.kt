package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.particle.colored.ColoredParticle
import dev.mim1q.derelict.particle.colored.ColoredParticleType
import dev.mim1q.derelict.particle.colored.SprayParticle
import dev.mim1q.derelict.particle.spider.SpiderEmitterParticle
import dev.mim1q.derelict.particle.spider.SpiderParticle
import dev.mim1q.derelict.particle.spider.SpiderParticleType
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModParticles {
    val SPRAY = register("spray", ColoredParticleType.create())
    val SPIDER = register("spider", SpiderParticleType.create())
    val SPIDER_EMITTER = register("spider_emitter", FabricParticleTypes.simple())

    fun init() {}

    fun initClient() {
        ParticleFactoryRegistry.getInstance().register(SPRAY, ColoredParticle.createFactory(::SprayParticle))
        ParticleFactoryRegistry.getInstance().register(SPIDER, SpiderParticle::Factory)
        ParticleFactoryRegistry.getInstance().register(SPIDER_EMITTER, SpiderEmitterParticle.Factory())
    }

    private fun <P, T : ParticleType<P>> register(name: String, particle: T): T =
        Registry.register(Registries.PARTICLE_TYPE, Derelict.id(name), particle)
}