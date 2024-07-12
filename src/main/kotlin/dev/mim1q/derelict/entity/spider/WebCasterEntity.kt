package dev.mim1q.derelict.entity.spider

import dev.mim1q.derelict.entity.boss.BigSpider
import dev.mim1q.derelict.entity.boss.BigSpiderAnimationProperties
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.world.World

class WebCasterEntity(entityType: EntityType<out SpiderEntity>, world: World) : SpiderEntity(entityType, world), BigSpider {
    override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)
}