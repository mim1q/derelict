package com.github.mim1q.derelict.item.graffiti

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

class SprayCanItem(settings: FabricItemSettings) : Item(settings.maxCount(1).maxDamage(16)) {
  override fun getUseAction(stack: ItemStack) = UseAction.SPYGLASS
  override fun getMaxUseTime(stack: ItemStack) = 60
  override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
    if (hand === Hand.OFF_HAND) return TypedActionResult.fail(user.getStackInHand(hand))
    return ItemUsage.consumeHeldItem(world, user, hand)
  }

  override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
    if (world?.isClient == true && user != null) {
      val vec = user.rotationVector
      val rotatedVec = vec.rotateY(MathHelper.RADIANS_PER_DEGREE * -90)
      val pos = user.pos.add(
        rotatedVec.x * 0.15 + vec.x * 0.2,
        1.75 + vec.y * 0.2,
        rotatedVec.z * 0.15 + vec.z * 0.2
      )
      val rng = user.random
      val velocity = vec.multiply(
        0.1 + rng.nextDouble() * 0.1,
        0.1 + rng.nextDouble() * 0.1,
        0.1 + rng.nextDouble() * 0.1
      )
      val particle = ParticleTypes.SMOKE
      world.addParticle(particle, pos.x, pos.y, pos.z, velocity.x, velocity.y, velocity.z)
    }
  }

  override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext?) {
    super.appendTooltip(stack, world, tooltip, context)
    val color = getColor(stack) ?: return
    val hexString = "#" + Integer.toHexString(color).padStart(6, '0')
    val colorText = Text.literal(hexString).setStyle(Style.EMPTY.withColor(color))
    tooltip.add(colorText)
  }

  companion object {
    fun getColor(stack: ItemStack): Int? {
      if (!stack.orCreateNbt.contains("color")) return null
      return stack.orCreateNbt.getInt("color")
    }
  }
}