package com.github.mim1q.derelict.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.DefaultParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class SmolderingEmbersBlock(
  settings: FabricBlockSettings,
  private val primaryParticle: DefaultParticleType = ParticleTypes.FLAME,
  private val secondaryParticle: DefaultParticleType = ParticleTypes.SMOKE,
  private val particleVelocity: Double = 0.01,
  private val particleRarity: Float = 0.2f
) : MultifaceGrowthBlock(settings.noCollision().ticksRandomly()), Waterloggable {
  override fun getGrower(): LichenGrower = null!!

  // Waterloggable
  companion object {
    val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
  }

  init {
    defaultState = defaultState.with(WATERLOGGED, false)
  }

  override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
    super.appendProperties(builder)
    builder.add(WATERLOGGED)
  }

  override fun getPlacementState(ctx: ItemPlacementContext) =
    super.getPlacementState(ctx)?.with(WATERLOGGED, false)

  override fun getFluidState(state: BlockState): FluidState {
    return if (state[WATERLOGGED]) Fluids.WATER.getStill(false) else super.getFluidState(state)
  }

  override fun getStateForNeighborUpdate(
    state: BlockState,
    direction: Direction,
    neighborState: BlockState,
    world: WorldAccess,
    pos: BlockPos,
    neighborPos: BlockPos
  ): BlockState {
    world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
  }

  // Particle Spawning
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