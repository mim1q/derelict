package com.github.mim1q.derelict.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.LichenGrower
import net.minecraft.block.Material
import net.minecraft.block.MultifaceGrowthBlock
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SmolderingEmbersBlock : MultifaceGrowthBlock(
  FabricBlockSettings.of(Material.FIRE).noCollision().ticksRandomly().luminance(4).emissiveLighting {_, _, _ -> true}
) {
  override fun getGrower(): LichenGrower = null!!

  private fun getBaseParticleOffset(state: BlockState, random: Random): Vec3d {
    val directions = collectDirections(state)
    val direction = directions.toList()[random.nextInt(directions.size)]
    val randomOffset = Vec3d(1.0, 1.0, 1.0)
      .subtract(Vec3d.of(direction.vector.multiply(direction.direction.offset())))
      .multiply(0.45)
      .multiply(random.nextDouble())
    return Vec3d.of(direction.vector).multiply(0.4).add(randomOffset)
  }

  override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
    if (random.nextFloat() < 0.8f) return
    val particlePos = getBaseParticleOffset(state, random).add(Vec3d.ofCenter(pos))
    val particle = if (random.nextFloat() < 0.4f) ParticleTypes.FLAME else ParticleTypes.SMOKE
    world.addParticle(particle, particlePos.x, particlePos.y, particlePos.z, 0.0, 0.01, 0.0)
  }
}