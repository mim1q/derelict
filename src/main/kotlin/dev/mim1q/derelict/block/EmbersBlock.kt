package dev.mim1q.derelict.block

import dev.mim1q.derelict.init.ModBlocksAndItems
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleEffect
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
sealed class EmbersBlock(settings: FabricBlockSettings, private val particleVelocity: Double = 0.01)
  : MultifaceGrowthBlock(settings.noCollision().ticksRandomly()), Waterloggable {
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
    super.getPlacementState(ctx)?.with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER)

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
    val particle = getParticle(random) ?: return
    val particlePos = getBaseParticleOffset(state, random).add(Vec3d.ofCenter(pos))
    world.addParticle(
      particle,
      particlePos.x, particlePos.y, particlePos.z,
      0.0, particleVelocity, 0.0
    )
  }

  abstract fun getParticle(random: Random): ParticleEffect?

  class Smoldering(settings: FabricBlockSettings): EmbersBlock(settings) {
    override fun getParticle(random: Random): ParticleEffect? {
      if (random.nextFloat() < 0.8f) return null
      return if (random.nextFloat() < 0.4f) ParticleTypes.FLAME else ParticleTypes.SMOKE
    }

    override fun tryFillWithFluid(world: WorldAccess, pos: BlockPos, state: BlockState, fluid: FluidState): Boolean {
      val canFillWithFluid = super.tryFillWithFluid(world, pos, state, fluid)
      if (canFillWithFluid) {
        world.setBlockState(pos, ModBlocksAndItems.SMOKING_EMBERS.getStateWithProperties(state).with(WATERLOGGED, true), 3)
      }
      return canFillWithFluid
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
      val state = super.getPlacementState(ctx) ?: return null
      if (state[WATERLOGGED]) {
        return ModBlocksAndItems.SMOKING_EMBERS.getStateWithProperties(state).with(WATERLOGGED, true)
      }
      return state
    }
  }

  class Smoking(settings: FabricBlockSettings): EmbersBlock(settings, 0.07) {
    override fun getParticle(random: Random): ParticleEffect = ParticleTypes.CAMPFIRE_COSY_SMOKE
  }
}