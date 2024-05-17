package dev.mim1q.derelict.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.LeavesBlock
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SmolderingLeavesBlock(settings: FabricBlockSettings) : LeavesBlock(
    settings.ticksRandomly().luminance(4).emissiveLighting { _, _, _ -> true }
) {
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        super.randomDisplayTick(state, world, pos, random)
        if (random.nextFloat() < 0.1f) {
            world.addParticle(
                ParticleTypes.FLAME,
                pos.x + random.nextDouble(),
                pos.y + random.nextDouble(),
                pos.z + random.nextDouble(),
                0.0,
                0.01,
                0.0
            )
        }
    }
}