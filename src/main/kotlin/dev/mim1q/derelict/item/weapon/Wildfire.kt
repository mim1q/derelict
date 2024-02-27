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
import kotlin.math.min

class Wildfire(settings: Settings) : Item(settings) {
  override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
    if (user !is PlayerEntity) return

    val shootFlame = user.getItemUseTime() >= 20
    if (shootFlame) {
      user.addVelocity(user.rotationVector.multiply(-1.0, 0.0, -1.0).multiply(0.03))
    }

    if (world !is ServerWorld) return

    val shakeStrength = min(1f, user.itemUseTime * 0.05f)

    user.yaw = 180f

    ScreenShakeUtils.shakeAround(
      world,
      user.pos,
      shakeStrength,
      40,
      5.0,
      32.0,
      "derelict_wildfire"
    )
  }

  override fun getUseAction(stack: ItemStack) = UseAction.NONE

  override fun getMaxUseTime(stack: ItemStack) = 3600 * 20

  override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> =
    ItemUsage.consumeHeldItem(world, user, hand)
}
