package com.github.mim1q.derelict.item

import com.github.mim1q.derelict.Derelict
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Oxidizable
import net.minecraft.item.HoneycombItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier

sealed class StaffItem(settings: FabricItemSettings) : Item(settings), CrosshairTipItem {
  private var lastBlock: Block? = null
  private var didShowTip: Boolean = false
  protected abstract val texture: Identifier

  override fun shouldShowTip(block: Block?): Boolean {
    if (block == null) return false
    if (block == lastBlock) return didShowTip
    lastBlock = block
    didShowTip = getBlockConversion(block) != null
    return didShowTip
  }
  override fun getTipTexture(): Identifier = texture
  abstract fun getBlockConversion(block: Block): Block?

  class Aging(settings: FabricItemSettings) : StaffItem(settings) {
    override val texture: Identifier = Identifier("textures/item/clock_00.png")

    override fun getBlockConversion(block: Block): Block? = firstNonNull(
      { Oxidizable.getIncreasedOxidationBlock(block).orElse(null) },
      { HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()[block]
        ?.let { Oxidizable.getIncreasedOxidationBlock(it).orElse(null) }
        ?.let { HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()[it] }
      }
    )
  }

  class Waxing(settings: FabricItemSettings) : StaffItem(settings) {
    override val texture: Identifier = Identifier("textures/item/honeycomb.png")

    override fun getBlockConversion(block: Block): Block? = firstNonNull(
      { HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()[block] }
    )
  }

  protected fun firstNonNull(vararg suppliers: () -> Block?): Block? {
    suppliers.forEach {
      val block = it()
      if (block != null) return block
    }
    return null
  }
}