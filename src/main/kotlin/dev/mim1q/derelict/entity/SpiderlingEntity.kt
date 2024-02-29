package dev.mim1q.derelict.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class SpiderlingEntity(entityType: EntityType<out HostileEntity>, world: World) : HostileEntity(entityType, world) {
  override fun initGoals() {
    super.initGoals()

    goalSelector.add(4, WanderAroundGoal(this, 1.0))
    goalSelector.add(5, WanderAroundFarGoal(this, 1.0))

    goalSelector.add(1, MeleeAttackGoal(this, 1.0, true))
    goalSelector.add(0, PounceAtTargetGoal(this, 0.3f))
    goalSelector.add(1, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))

    targetSelector.add(0, ActiveTargetGoal(this, SheepEntity::class.java, true))
    targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, true))
  }

  override fun attackLivingEntity(target: LivingEntity) {
    super.attackLivingEntity(target)
  }

  override fun tryAttack(target: Entity?): Boolean {
     if (super.tryAttack(target)) {
       damage(world.damageSources.generic(), 2.0f)
       return true
     }

    return false
  }

  companion object {
    fun createSpiderlingAttributes(): DefaultAttributeContainer.Builder {
      return createHostileAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 2.0)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.33)
    }
  }
}

