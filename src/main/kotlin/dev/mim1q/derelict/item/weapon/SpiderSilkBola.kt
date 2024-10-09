package dev.mim1q.derelict.item.weapon

import dev.mim1q.derelict.entity.projectile.SpiderSilkBolaProjectile
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World

class SpiderSilkBola(settings: Settings) : Item(settings) {
    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks)

        val ticks = getMaxUseTime(stack) - remainingUseTicks
        if (ticks < 10) return

        if (!world.isClient) {
            val projectile = SpiderSilkBolaProjectile(ModEntities.SPIDER_SILK_BOLA, world)
            projectile.setPosition(user.pos.add(0.0, 1.5, 0.0))
            projectile.owner = user
            projectile.setVelocity(user, user.pitch, user.yaw, 0f, 1.5f, 0.1f)
            world.spawnEntity(projectile)

            world.playSound(
                null,
                user.blockPos,
                SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS,
                1f,
                0.6f + world.random.nextFloat() * 0.2f
            )
            user.getStackInHand(user.activeHand).decrement(1)
            (user as? ServerPlayerEntity)?.itemCooldownManager?.set(this, 20)
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
        ItemUsage.consumeHeldItem(world, user, hand)

    override fun getMaxUseTime(stack: ItemStack): Int = 3600 * 20
    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW
}