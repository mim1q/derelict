package dev.mim1q.derelict.item.weapon

import dev.mim1q.derelict.block.cobweb.SpiderEggBlock
import dev.mim1q.derelict.entity.projectile.SpiderEggProjectile
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.world.World

const val MAX_EGGS = 20
const val MAX_USE_TIME = 60 * 60 * 20

class Arachnicannon(settings: Settings) : Item(settings) {
    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks)

        val time = MAX_USE_TIME - remainingUseTicks

        if (!world.isClient && time > 10) {
            val count = stack.getLoadedEggs()
            val amount = count.coerceAtMost(5)
            if (amount == 0) return

            stack.setLoadedEggs(count - amount)
            stack.damage(1, user) {
                user.sendToolBreakStatus(user.activeHand)
            }

            repeat(amount) {
                val projectile = SpiderEggProjectile(ModEntities.SPIDER_EGG_PROJECTILE, world)
                projectile.setPosition(user.pos.add(0.0, 1.5, 0.0))
                projectile.owner = user
                projectile.setVelocity(user, user.pitch, user.yaw, 0f, 2.3f + user.random.nextFloat() * 2f, 0.4f)
                world.spawnEntity(projectile)
            }

            (user as? ServerPlayerEntity)?.itemCooldownManager?.set(this, 200)
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> =
        user.getStackInHand(hand).let {
            if (it.getLoadedEggs() == 0) {
                TypedActionResult.fail(it)
            } else {
                ItemUsage.consumeHeldItem(world, user, hand)
            }
        }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val state = context.world.getBlockState(context.blockPos)
        if (state.block is SpiderEggBlock && context.stack.getLoadedEggs() < MAX_EGGS) {
            context.world.breakBlock(context.blockPos, false)
            context.stack.setLoadedEggs(context.stack.getLoadedEggs() + 1)
            context.world.playSound(
                null,
                context.blockPos,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
            )
            return ActionResult.SUCCESS
        }
        return super.useOnBlock(context)
    }

    override fun getMaxUseTime(stack: ItemStack): Int = MAX_USE_TIME
    override fun getUseAction(stack: ItemStack): UseAction = UseAction.BOW

    private fun ItemStack.getLoadedEggs(): Int = nbt?.getInt("eggs") ?: 0
    private fun ItemStack.setLoadedEggs(eggs: Int) = getOrCreateNbt().putInt("eggs", eggs)

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        val loaded = stack.getLoadedEggs()
        if (loaded == 0) {
            tooltip.add(
                Text.translatable("item.derelict.arachnicannon.tooltip.no_eggs")
                    .formatted(Formatting.GRAY)
            )
        }
        tooltip.add(
            Text.translatable("item.derelict.arachnicannon.tooltip.eggs", loaded, MAX_EGGS)
                .formatted(Formatting.GRAY)
        )
    }
}