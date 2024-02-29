package dev.mim1q.derelict.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.ActiveTargetGoal
import net.minecraft.entity.ai.goal.MeleeAttackGoal
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.world.World

class SpiderlingEntity(entityType: EntityType<out HostileEntity>, world: World) : HostileEntity(entityType, world) {
  override fun initGoals() {
    super.initGoals()

    goalSelector.add(0, MeleeAttackGoal(this, 0.5, true))

    targetSelector.add(0, ActiveTargetGoal(this, SheepEntity::class.java, true))
  }
}