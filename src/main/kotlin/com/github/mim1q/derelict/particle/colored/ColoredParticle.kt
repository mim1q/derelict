package com.github.mim1q.derelict.particle.colored

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.particle.SpriteProvider
import net.minecraft.client.world.ClientWorld


abstract class ColoredParticle(
  world: ClientWorld,
  x: Double,
  y: Double,
  z: Double,
  velocityX: Double,
  velocityY: Double,
  velocityZ: Double,
  color: Int,
) : SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
  protected var spriteProvider: SpriteProvider? = null
  init {
    val r = color shr 16
    val g = color shr 8 and 0xFF
    val b = color and 0xFF
    red = r / 255.0f
    green = g / 255.0f
    blue = b / 255.0f
  }

  fun interface ColoredParticleConstructor {
    fun create(
      world: ClientWorld,
      x: Double,
      y: Double,
      z: Double,
      velocityX: Double,
      velocityY: Double,
      velocityZ: Double,
      color: Int
    ): ColoredParticle
  }

  class Factory(private val spriteProvider: SpriteProvider, private val constructor: ColoredParticleConstructor) :
    ParticleFactory<ColoredParticleEffect> {
    override fun createParticle(
      parameters: ColoredParticleEffect,
      world: ClientWorld,
      x: Double,
      y: Double,
      z: Double,
      velocityX: Double,
      velocityY: Double,
      velocityZ: Double
    ): Particle {
      val particle = constructor.create(world, x, y, z, velocityX, velocityY, velocityZ, parameters.color)
      particle.setSprite(spriteProvider)
      particle.spriteProvider = spriteProvider
      return particle
    }
  }

  companion object {
    fun createFactory(constructor: ColoredParticleConstructor): PendingParticleFactory<ColoredParticleEffect> {
      return PendingParticleFactory { spriteProvider: FabricSpriteProvider -> Factory(spriteProvider, constructor) }
    }
  }
}