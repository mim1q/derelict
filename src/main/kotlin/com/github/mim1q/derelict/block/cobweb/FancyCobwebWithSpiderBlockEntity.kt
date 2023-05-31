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

class FancyCobwebWithSpiderBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntities.FANCY_COBWEB_WITH_SPIDER, pos, state) {
  var lastLoweringProgress = 0.0
  var loweringProgress = 0.0
  var distance = 0

  fun tick(world: World, pos: BlockPos, state: BlockState, shy: Boolean) {
    if (world.isClient) {
      lastLoweringProgress = loweringProgress
      val multiplier = if (state[FancyCobwebWithSpiderBlock.LOWERING]) 1.0 else -1.0
      loweringProgress += multiplier * 0.02
      loweringProgress = MathHelper.clamp(loweringProgress, 0.0, 1.0)
      return
    }
    if (world.time % 20 == 0L) {
      var availableBlocks = 0
      for (i in 1..16) {
        if (world.getBlockState(pos.down(i)).isAir) availableBlocks++ else break
      }
      if (availableBlocks != distance) {
        distance = availableBlocks
        markDirty()
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS)
      }
      if (availableBlocks == 0) {
        world.setBlockState(pos, state.with(FancyCobwebWithSpiderBlock.LOWERING, false))
        return
      }
      val nearestPlayer = world.getClosestPlayer(
        pos.x + 0.5, pos.y + 1.5 - availableBlocks, pos.z + 0.5, 5.0, false
      )
      world.setBlockState(pos, state.with(FancyCobwebWithSpiderBlock.LOWERING, (nearestPlayer != null) xor shy))
    }
  }

  override fun readNbt(nbt: NbtCompound) {
    super.readNbt(nbt)
    distance = nbt.getInt("Distance")
  }

  override fun writeNbt(nbt: NbtCompound) {
    super.writeNbt(nbt)
    nbt.putInt("Distance", distance)
  }

  override fun toInitialChunkDataNbt(): NbtCompound = createNbt()
  override fun toUpdatePacket(): Packet<ClientPlayPacketListener> = BlockEntityUpdateS2CPacket.create(this)
}