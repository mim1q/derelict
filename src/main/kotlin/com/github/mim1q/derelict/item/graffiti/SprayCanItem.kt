package com.github.mim1q.derelict.item.graffiti

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class SprayCanItem(settings: FabricItemSettings) : Item(settings.maxCount(1).maxDamage(16)) {
  override fun getUseAction(stack: ItemStack) = UseAction.SPYGLASS
  override fun getMaxUseTime(stack: ItemStack) = 60
  override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
    ItemUsage.consumeHeldItem(world, user, hand)
}