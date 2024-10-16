package dev.mim1q.derelict.item.weapon

import dev.mim1q.derelict.block.cobweb.SpiderEggBlock
import dev.mim1q.derelict.entity.projectile.SpiderEggProjectile
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

const val MAX_EGGS = 20

class Arachnicannon(settings: Settings) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {

            val count = user.getStackInHand(hand).getLoadedEggs()
            val amount = count.coerceAtMost(5)
            if (amount == 0) return TypedActionResult.fail(user.getStackInHand(hand))

            user.getStackInHand(hand).setLoadedEggs(count - amount)

            repeat(amount) {
                val projectile = SpiderEggProjectile(ModEntities.SPIDER_EGG_PROJECTILE, world)
                projectile.setPosition(user.pos.add(0.0, 1.5, 0.0))
                projectile.owner = user
                projectile.setVelocity(user, user.pitch, user.yaw, 0f, 2.3f + user.random.nextFloat() * 2f, 0.4f)
                world.spawnEntity(projectile)
            }

            user.itemCooldownManager.set(this, 20)
        }

        return TypedActionResult.consume(user.getStackInHand(hand))
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

    private fun ItemStack.getLoadedEggs(): Int = nbt?.getInt("eggs") ?: 0
    private fun ItemStack.setLoadedEggs(eggs: Int) = getOrCreateNbt().putInt("eggs", eggs)
}