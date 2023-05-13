package com.github.mim1q.derelict.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.LichenGrower
import net.minecraft.block.MultifaceGrowthBlock
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class SmolderingEmbersBlock(
  settings: FabricBlockSettings,
  private val primaryParticle: DefaultParticleType = ParticleTypes.FLAME,
  private val secondaryParticle: DefaultParticleType = ParticleTypes.SMOKE,
  private val particleVelocity: Double = 0.01,
  private val particleRarity: Float = 0.2f
) : MultifaceGrowthBlock(
  settings.noCollision().ticksRandomly()
) {
  override fun getGrower(): LichenGrower = null!!

  private fun getBaseParticleOffset(state: BlockState, random: Random): Vec3d {
    val directions = collectDirections(state)
    val direction = directions.toList()[random.nextInt(directions.size)]
    val planeVector = Vec3d(1.0, 1.0, 1.0)
      .subtract(Vec3d.of(direction.vector.multiply(direction.direction.offset())))
    val randomOffset = planeVector
      .multiply(random.nextDouble(), random.nextDouble(), random.nextDouble())
      .subtract(planeVector.multiply(0.4))
    return Vec3d.of(direction.vector).multiply(0.35).add(randomOffset)
  }

  override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
    if (random.nextFloat() > particleRarity) return
    val particlePos = getBaseParticleOffset(state, random).add(Vec3d.ofCenter(pos))
    val particle = if (random.nextFloat() < 0.6f) primaryParticle else secondaryParticle
    world.addParticle(
      particle,
      particlePos.x, particlePos.y, particlePos.z,
      0.0, particleVelocity, 0.0
    )
  }
}