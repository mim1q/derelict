package dev.mim1q.derelict.item.weapon

import dev.mim1q.derelict.entity.projectile.SpiderSilkBolaProjectile
import dev.mim1q.derelict.init.ModEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class SpiderSilkBola(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            val projectile = SpiderSilkBolaProjectile(ModEntities.SPIDER_SILK_BOLA, world)
            projectile.setPosition(user.pos.add(0.0, 1.5, 0.0))
            projectile.owner = user
            projectile.setVelocity(user, user.pitch, user.yaw, 0f, 1.5f, 0.1f)
            world.spawnEntity(projectile)
        }

        return TypedActionResult.consume(user.getStackInHand(hand))
    }
}