package dev.mim1q.derelict.item

import dev.mim1q.derelict.init.ModEntities
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class SpiderlingBucketItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.world.isClient) return ActionResult.SUCCESS
        ModEntities.SPIDERLING.create(context.world)?.let { spiderling ->
            spiderling.setPosition(Vec3d.ofCenter(context.blockPos.add(context.side.vector)))
            if (context.side == Direction.DOWN) {
                spiderling.anchorPosition = context.blockPos
            }
            spiderling.setPersistent()
            context.world.spawnEntity(spiderling)
        }
        return ActionResult.CONSUME
    }
}