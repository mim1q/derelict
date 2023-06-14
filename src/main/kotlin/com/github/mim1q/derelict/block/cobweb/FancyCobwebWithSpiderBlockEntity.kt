package com.github.mim1q.derelict.block.cobweb

import com.github.mim1q.derelict.init.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.util.*
import kotlin.math.atan2
import kotlin.math.max

class FancyCobwebWithSpiderBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.FANCY_COBWEB_WITH_SPIDER, pos, state) {
  private val rng = Random(pos.hashCode().toLong())
  private val distanceFromGround = 0.75 + rng.nextDouble(2.0)
  private val speed = 0.01 + rng.nextDouble(0.015)
  private val delay = rng.nextInt(40)
  private val range = 4.0 + rng.nextDouble(3.0)
  val scale = 0.8F + rng.nextFloat(0.4F)

  var lastLoweringProgress = 0.0
    private set
  var loweringProgress = 0.0
    private set
  var distance = 0.0
    private set
  var lastClientYaw = 0F
    private set
  var clientYaw = 0F
    private set

  fun tick(world: World, pos: BlockPos, state: BlockState, shy: Boolean) {
    if (world.isClient) {
      lastLoweringProgress = loweringProgress
      lastClientYaw = clientYaw
      val multiplier = if (state[FancyCobwebWithSpiderBlock.LOWERING]) 1.0 else -1.0
      loweringProgress += multiplier * speed
      loweringProgress = MathHelper.clamp(loweringProgress, 0.0, 1.0)
      val player = world.getClosestPlayer(pos.x + 0.5, pos.y + 0.5 - distance, pos.z + 0.5, 16.0, false)
      if (player != null) {
        val yaw = atan2(
          player.z - pos.z - 0.5,
          player.x - pos.x - 0.5
        ).toFloat() * MathHelper.DEGREES_PER_RADIAN + 90F
        clientYaw = MathHelper.lerpAngleDegrees(speed.toFloat() * 2F, clientYaw, yaw)
      }
      return
    }
    if ((world.time + delay) % 40 == 0L) {
      var availableBlocks = 0
      for (i in 1..16) {
        if (!world.getBlockState(pos.down(i)).isSolidBlock(world, pos)) availableBlocks++ else break
      }
      val newDistance = max(0.0, availableBlocks.toDouble() - distanceFromGround)
      if (newDistance != distance) {
        distance = newDistance
        markDirty()
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
      }
      if (availableBlocks == 0) {
        world.setBlockState(pos, state.with(FancyCobwebWithSpiderBlock.LOWERING, false))
        return
      }
      val nearestPlayer = world.getClosestPlayer(
        pos.x + 0.5,
        pos.y + 0.5 - distance,
        pos.z + 0.5,
        range,
        false
      )
      world.setBlockState(pos, state.with(FancyCobwebWithSpiderBlock.LOWERING, (nearestPlayer != null) xor shy))
    }
  }

  override fun readNbt(nbt: NbtCompound) {
    super.readNbt(nbt)
    distance = nbt.getDouble("Distance")
  }

  override fun writeNbt(nbt: NbtCompound) {
    super.writeNbt(nbt)
    nbt.putDouble("Distance", distance)
  }

  override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
  override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)
}