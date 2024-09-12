package dev.mim1q.derelict.particle.spider

import dev.mim1q.derelict.init.ModParticles
import net.minecraft.client.MinecraftClient
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction

class SpiderEmitterParticle private constructor(
    world: ClientWorld, x: Double, y: Double, z: Double
) : NoRenderParticle(world, x, y, z) {
    override fun tick() {
        val blockPos = BlockPos.Mutable(x.toInt(), y.toInt(), z.toInt())
        val player = MinecraftClient.getInstance().player
        if (player == null) {
            markDead()
            return
        }

        val direction = player.pos
            .subtract(x, y, z)
            .multiply(1.0, 0.0, 1.0)
            .normalize()
            .multiply(random.nextDouble() * 0.05 + 0.02)

        repeat(10) {
            if (
                world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP)
                && world.getBlockState(blockPos.up()).isAir
            ) {
                world.addParticle(
                    ModParticles.SPIDER.get(Direction.DOWN),
                    x, blockPos.y + 1.1, z,
                    direction.x, direction.y, direction.z
                )
                markDead()
                return
            }

            blockPos.move(0, -1, 0)
        }
        markDead()
    }

    class Factory : ParticleFactory<DefaultParticleType> {
        override fun createParticle(
            defaultParticleType: DefaultParticleType,
            clientWorld: ClientWorld,
            d: Double,
            e: Double,
            f: Double,
            g: Double,
            h: Double,
            i: Double
        ): Particle {
            return SpiderEmitterParticle(clientWorld, d, e, f)
        }
    }
}