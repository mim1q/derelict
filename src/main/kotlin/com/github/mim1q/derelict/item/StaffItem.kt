package com.github.mim1q.derelict.item

import com.github.mim1q.derelict.Derelict
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Oxidizable
import net.minecraft.client.util.ParticleUtil
import net.minecraft.item.HoneycombItem
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.util.math.intprovider.UniformIntProvider

sealed class StaffItem(settings: FabricItemSettings) : Item(settings), CrosshairTipItem {
  private var lastBlock: Block? = null
  private var didShowTip: Boolean = false
  protected abstract val texture: Identifier
  protected abstract val particle: ParticleEffect
  protected abstract val sound: SoundEvent

  override fun shouldShowTip(block: Block?): Boolean {
    if (block == null) return false
    if (block == lastBlock) return didShowTip
    lastBlock = block
    didShowTip = getBlockConversion(block) != null
    return didShowTip
  }
  override fun getTipTexture(): Identifier = texture
  abstract fun getBlockConversion(block: Block): Block?

  override fun useOnBlock(context: ItemUsageContext): ActionResult {
    val world = context.world
    val pos = context.blockPos
    val state = world.getBlockState(pos)
    val conversion = getBlockConversion(state.block)
    if (conversion != null) {
      if (world.isClient) ParticleUtil.spawnParticle(world, pos, particle, UniformIntProvider.create(3, 5))
      world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1F, 1F)
      world.setBlockState(context.blockPos, conversion.getStateWithProperties(state))
      return ActionResult.SUCCESS
    }
    return ActionResult.PASS
  }

  class Aging(settings: FabricItemSettings) : StaffItem(settings) {
    override val texture: Identifier = Identifier("textures/item/clock_00.png")
    override val particle: ParticleEffect = ParticleTypes.WHITE_ASH
    override val sound: SoundEvent = SoundEvents.BLOCK_STONE_BREAK

    override fun getBlockConversion(block: Block): Block? = firstNonNull(
      { Oxidizable.getIncreasedOxidationBlock(block).orElse(null) },
      { HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()[block]
        ?.let { Oxidizable.getIncreasedOxidationBlock(it).orElse(null) }
        ?.let { HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()[it] }
      }
    )

    override fun shouldShowTip(block: Block?)
      = Derelict.CLIENT_CONFIG.ageableCrosshairTip() && super.shouldShowTip(block)
  }

  class Waxing(settings: FabricItemSettings) : StaffItem(settings) {
    override val texture: Identifier = Identifier("textures/item/honeycomb.png")
    override val particle: ParticleEffect = ParticleTypes.WAX_ON
    override val sound: SoundEvent = SoundEvents.ITEM_HONEYCOMB_WAX_ON

    override fun getBlockConversion(block: Block): Block? = firstNonNull(
      { HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()[block] }
    )

    override fun shouldShowTip(block: Block?)
      = Derelict.CLIENT_CONFIG.waxableCrosshairTip() && super.shouldShowTip(block)
  }

  protected fun firstNonNull(vararg suppliers: () -> Block?): Block? {
    suppliers.forEach {
      val block = it()
      if (block != null) return block
    }
    return null
  }
}