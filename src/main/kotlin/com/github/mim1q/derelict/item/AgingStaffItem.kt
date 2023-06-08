package com.github.mim1q.derelict.item

import net.minecraft.block.Oxidizable
import net.minecraft.item.HoneycombItem
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class AgingStaffItem(settings: Settings) : Item(settings) {
  override fun useOnBlock(context: ItemUsageContext): ActionResult {
    var block = context.world.getBlockState(context.blockPos).block
    var waxed = false
    HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()[block]?.let {
      waxed = true
      block = it
    }
    val map = Oxidizable.OXIDATION_LEVEL_INCREASES.get()
    if (map.containsKey(block)) {
      var newBlock = map[block]!!
      if (waxed) {
        newBlock = HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get()[newBlock] ?: newBlock
      }
      context.world.setBlockState(context.blockPos, newBlock.defaultState)
      context.stack.damage(1, context.player) { context.player?.sendToolBreakStatus(context.hand) }
      return ActionResult.SUCCESS
    }
    return ActionResult.PASS
  }
}