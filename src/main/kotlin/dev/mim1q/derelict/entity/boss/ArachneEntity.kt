package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.spider.control.ArachneBodyControl
import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import dev.mim1q.derelict.util.wrapDegrees
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world) {
    val legController = SpiderLegController(
        this,
        24 / 16f,
        28 / 16f,
        Vec3d(0.6, 0.9, 1.0) to Vec3d(1.5, 0.0, 3.2),
        Vec3d(0.6, 1.0, 0.65) to Vec3d(2.5, 0.0, 1.5),
        Vec3d(0.6, 1.1, 0.3) to Vec3d(2.0, 0.0, 0.5),
        Vec3d(0.6, 1.2, 0.0) to Vec3d(1.5, 0.0, -1.0),

        Vec3d(-0.6, 0.9, 1.0) to Vec3d(-1.5, 0.0, 3.2),
        Vec3d(-0.6, 1.0, 0.65) to Vec3d(-2.5, 0.0, 1.5),
        Vec3d(-0.6, 1.1, 0.3) to Vec3d(-2.0, 0.0, -0.5),
        Vec3d(-0.6, 1.2, 0.0) to Vec3d(-1.5, 0.0, -1.0),
    )

    override fun initGoals() {
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 24F))
        goalSelector.add(4, LookAroundGoal(this))
        goalSelector.add(2, WanderAroundGoal(this, 0.4))
        goalSelector.add(2, WanderAroundFarGoal(this, 0.4))
        goalSelector.add(0, MeleeAttackGoal(this, 0.6, true))
        targetSelector.add(0, ActiveTargetGoal(this, PlayerEntity::class.java, false))
    }

    override fun tick() {
        if (world.isClient) {
            legController.tick()
        }
        super.tick()
    }

    override fun setBodyYaw(bodyYaw: Float) {
        super.setBodyYaw(wrapDegrees(this.bodyYaw, bodyYaw, 10f))
    }

    override fun createBodyControl() = ArachneBodyControl(this)

    companion object {
        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
    }
}