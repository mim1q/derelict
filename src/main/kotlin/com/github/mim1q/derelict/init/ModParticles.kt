package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.particle.SpiderParticle
import com.github.mim1q.derelict.particle.colored.ColoredParticle
import com.github.mim1q.derelict.particle.colored.ColoredParticleType
import com.github.mim1q.derelict.particle.colored.SprayParticle
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.util.registry.Registry

object ModParticles {
  val SPRAY = register("spray", ColoredParticleType.create())
  val SPIDER = register("spider", FabricParticleTypes.simple())

  fun init() { }

  fun initClient() {
    ParticleFactoryRegistry.getInstance().register(SPRAY, ColoredParticle.createFactory(::SprayParticle))
    ParticleFactoryRegistry.getInstance().register(SPIDER, SpiderParticle::Factory)
  }

  private fun <P, T : ParticleType<P>> register(name: String, particle: T): T =
    Registry.register(Registry.PARTICLE_TYPE, Derelict.id(name), particle)
}