package dev.mim1q.derelict.item.weapon

import dev.mim1q.gimm1q.screenshake.ScreenShakeUtils
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class Wildfire(settings: Settings) : Item(settings) {
  override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
    if (world !is ServerWorld || user !is PlayerEntity) return

    println(user.itemUseTime)

    val shootFlame = user.getItemUseTime() >= 20
    ScreenShakeUtils.shakeAround(
      world,
      user.pos,
      if (shootFlame) 2f else 0.2f,
      20,
      5.0,
      32.0,
      "derelict_wildfire"
    )
  }

  override fun getUseAction(stack: ItemStack) = UseAction.CROSSBOW

  override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack>
    = ItemUsage.consumeHeldItem(world, user, hand)
}
