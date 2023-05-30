package com.github.mim1q.derelict.particle

import net.minecraft.client.particle.*
import net.minecraft.client.render.Camera
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2

class SpiderParticle(
  world: ClientWorld,
  x: Double,
  y: Double,
  z: Double,
  private val vx: Double,
  private val vy: Double,
  private val vz: Double,
  private val spriteProvider: SpriteProvider
) : SpriteBillboardParticle(world, x, y, z, 0.0, 0.0, 0.0) {
  init {
    maxAge = 40 + random.nextInt(40)
    alpha = 0.0F
    scale = random.nextFloat() * 0.15F + 0.1F
  }

  override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

  override fun buildGeometry(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
    val rotation = Math.toDegrees(atan2(velocityZ, velocityX))
    FakeCamera.setRotationAndPosition(rotation.toFloat() - 90.0F, 90.0F, camera.pos.add(0.0, -0.01, 0.0))
    super.buildGeometry(vertexConsumer, FakeCamera, tickDelta)
  }

  override fun tick() {
    setVelocity(vx, vy, vz)
    super.tick()
    setSprite(spriteProvider.getSprite(age / 2 % 4, 3))
    if (age <= 5) {
      alpha = (age / 5.0F)
    }
    val remaining = maxAge - age
    if (remaining <= 5) {
      alpha = (remaining / 5.0F)
    }
    if (world.getBlockCollisions(null, boundingBox.offset(0.0, -0.1, 0.0)).none()) {
      markDead()
    }
  }

  class Factory(private val spriteProvider: SpriteProvider) : ParticleFactory<DefaultParticleType> {
    override fun createParticle(
      parameters: DefaultParticleType,
      world: ClientWorld,
      x: Double,
      y: Double,
      z: Double,
      velocityX: Double,
      velocityY: Double,
      velocityZ: Double
    ): Particle {
      val particle = SpiderParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider)
      particle.setSprite(spriteProvider)
      return particle
    }
  }

  private object FakeCamera : Camera() {
    init {
      setRotation(0.0F, 90.0F)
    }
    fun setRotationAndPosition(yaw: Float, pitch: Float, pos: Vec3d) {
      setRotation(yaw, pitch)
      this.pos = pos
    }
  }
}