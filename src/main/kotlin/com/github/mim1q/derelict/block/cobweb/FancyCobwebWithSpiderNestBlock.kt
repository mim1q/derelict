package com.github.mim1q.derelict.block.cobweb

import com.github.mim1q.derelict.init.ModParticles
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class FancyCobwebWithSpiderNestBlock(settings: Settings) : FancyCobwebBlock(settings) {
  private fun spawnParticle(world: WorldAccess, pos: BlockPos, random: Random, chance: Float) {
    if (random.nextFloat() > chance) return
    val vel = Vec3d(random.nextDouble() - 0.5, 0.0, random.nextDouble() - 0.5)
      .normalize()
      .multiply(random.nextDouble() * 0.05 + 0.05)
    world.addParticle(ModParticles.SPIDER, pos.x + random.nextDouble(), pos.y + 0.05, pos.z + random.nextDouble(), vel.x, vel.y, vel.z)
  }

  override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
    spawnParticle(world, pos, random, 0.25F)
  }

  @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
  override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
    if (world.isClient) {
      repeat(2) {
        spawnParticle(world, pos, world.random, 0.5F)
      }
    }
    super.onEntityCollision(state, world, pos, entity)
  }

  override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState) {
    super.onBroken(world, pos, state)
    repeat(40) {
      spawnParticle(world, pos, world.random, 1.0F)
    }
  }
}