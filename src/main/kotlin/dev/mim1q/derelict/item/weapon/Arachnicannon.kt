package dev.mim1q.derelict.item.weapon

import dev.mim1q.derelict.entity.projectile.SpiderEggProjectile
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class Arachnicannon(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val projectile = SpiderEggProjectile(ModEntities.SPIDER_EGG_PROJECTILE, world)
            projectile.setPosition(user.pos.add(0.0, 1.5, 0.0))
            projectile.owner = user
            projectile.setVelocity(user, user.pitch, user.yaw, 0f, 2.5f, 0.3f)
            world.spawnEntity(projectile)

            user.itemCooldownManager.set(this, 20)
        }

        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}