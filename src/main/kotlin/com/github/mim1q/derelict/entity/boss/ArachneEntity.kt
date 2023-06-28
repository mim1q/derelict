package com.github.mim1q.derelict.entity.boss

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world), BigSpider {
  override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)

  init {
    stepHeight = 2F
  }

  override fun initGoals() {
    goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
    goalSelector.add(4, LookAroundGoal(this))
    goalSelector.add(2, WanderAroundGoal(this, 0.4))
    goalSelector.add(2, WanderAroundFarGoal(this, 0.4))
    goalSelector.add(0, MeleeAttackGoal(this, 0.6, true))
    targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, false))
  }

  override fun tick() {
    super.tick()
    if (world.isClient) bigSpiderAnimationProperties.tick()
  }

  override fun createBodyControl() = ArachneBodyControl(this)

  companion object {
    fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
      .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
  }
}