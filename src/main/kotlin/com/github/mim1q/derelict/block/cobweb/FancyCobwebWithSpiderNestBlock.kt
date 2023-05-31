package com.github.mim1q.derelict.block.cobweb

import com.github.mim1q.derelict.init.ModParticles
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class FancyCobwebWithSpiderNestBlock(settings: Settings) : FancyCobwebBlock(settings) {
  private fun spawnParticle(state: BlockState, world: WorldAccess, blockPos: BlockPos, random: Random, chance: Float, speed: Double) {
    if (random.nextFloat() > chance) return
    val vel = Vec3d(random.nextDouble() - 0.5, 0.0, random.nextDouble() - 0.5)
      .normalize()
      .multiply((random.nextDouble() + 1) * speed)
    val directions = getDirections(state)
    if (directions.isEmpty()) return
    val direction = directions.toList()[random.nextInt(directions.size)]
    val pos = Vec3d.ofCenter(blockPos).add(Vec3d.of(direction.vector).multiply(0.49))
    world.addParticle(ModParticles.SPIDER.get(direction), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z)
  }

  private fun getDirections(state: BlockState) = Direction.values().filter { state[getDirectionProperty(it)] }.toList()

  override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
    spawnParticle(state, world, pos, random, 0.5F, 0.02)
  }

  @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
  override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
    if (world.isClient && entity.isLiving) {
      val times = if (entity.prevX != entity.x || entity.prevY != entity.y || entity.prevZ != entity.z) 6 else 2
      repeat(times) {
        spawnParticle(state, world, pos, world.random, 0.5F, 0.04)
      }
    }
    super.onEntityCollision(state, world, pos, entity)
  }

  override fun onBroken(world: WorldAccess, pos: BlockPos, state: BlockState) {
    super.onBroken(world, pos, state)
    repeat(40) {
      spawnParticle(state, world, pos, world.random, 1.0F, 0.06)
    }
  }
}