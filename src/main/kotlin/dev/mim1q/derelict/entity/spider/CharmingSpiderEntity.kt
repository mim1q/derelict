package dev.mim1q.derelict.entity.spider

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.world.World

class CharmingSpiderEntity(
    entityType: EntityType<out SpiderEntity>,
    world: World
) : SpiderEntity(entityType, world) {
}