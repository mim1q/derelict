package com.github.mim1q.derelict.item

import net.minecraft.block.Oxidizable
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult

class AgingStaffItem(settings: Settings) : Item(settings) {
  override fun useOnBlock(context: ItemUsageContext): ActionResult {
    val block = context.world.getBlockState(context.blockPos).block
    val map = Oxidizable.OXIDATION_LEVEL_INCREASES.get()
    if (map.containsKey(block)) {
      val newBlock = map[block]!!
      context.world.setBlockState(context.blockPos, newBlock.defaultState)
      context.stack.damage(1, context.player) { context.player?.sendToolBreakStatus(context.hand) }
      return ActionResult.SUCCESS
    }
    return ActionResult.PASS
  }
}