package dev.mim1q.derelict.entity.spider

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.LookAroundGoal
import net.minecraft.entity.ai.goal.LookAtEntityGoal
import net.minecraft.entity.ai.goal.WanderAroundFarGoal
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class DaddyLongLegsEntity(
    entityType: EntityType<DaddyLongLegsEntity>,
    world: World
) : PathAwareEntity(entityType, world) {
    override fun initGoals() {
        super.initGoals()

        goalSelector.add(4, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(3, LookAroundGoal(this))
    }
}