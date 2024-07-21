package dev.mim1q.derelict.entity.spider

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.world.World

class JumpingSpiderEntity(
    entityType: EntityType<out JumpingSpiderEntity>,
    world: World
) : SpiderEntity(entityType, world) {

}