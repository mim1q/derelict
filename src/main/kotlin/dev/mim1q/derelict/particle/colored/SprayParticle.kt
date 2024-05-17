package dev.mim1q.derelict.particle.colored

import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.world.ClientWorld

class SprayParticle(
    world: ClientWorld,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    color: Int
) : ColoredParticle(world, x, y, z, velocityX, velocityY, velocityZ, color) {
    init {
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.velocityZ = velocityZ
        this.scale = 0.1F + world.getRandom().nextFloat() * 0.1F
        this.maxAge = 30
        val multiplier = 1.0f - random.nextFloat() * 0.25f
        red *= multiplier
        green *= multiplier
        blue *= multiplier
    }

    override fun tick() {
        super.tick()
        val progress = age / maxAge.toFloat()
        velocityMultiplier = 1.0f - progress * 0.2f
        setSpriteForAge(this.spriteProvider)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_OPAQUE
}