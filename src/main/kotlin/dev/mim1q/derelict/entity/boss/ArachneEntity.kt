package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.spider.legs.SpiderLegController
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper.lerpAngleDegrees
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.lang.Math.min
import kotlin.math.abs

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world), BigSpider {
    override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)

    val legController = SpiderLegController(
        this,
        22 / 16f,
        30 / 16f,
        Vec3d(0.6, 0.9, 1.0) to Vec3d(1.5, 0.0, 3.5),
        Vec3d(0.6, 1.1, 0.65) to Vec3d(2.5, 0.0, 1.5),
        Vec3d(0.6, 1.3, 0.3) to Vec3d(2.0, 0.0, 0.5),
        Vec3d(0.6, 1.5, 0.0) to Vec3d(1.5, 0.0, -1.0),

        Vec3d(-0.6, 0.9, 1.0) to Vec3d(-1.5, 0.0, 3.5),
        Vec3d(-0.6, 1.1, 0.65) to Vec3d(-2.5, 0.0, 1.5),
        Vec3d(-0.6, 1.3, 0.3) to Vec3d(-2.0, 0.0, -0.5),
        Vec3d(-0.6, 1.5, 0.0) to Vec3d(-1.5, 0.0, -1.0),
    )

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

        val angleDiff = min(abs(bodyYaw - prevBodyYaw), 360 - abs(bodyYaw - prevBodyYaw))

        if (angleDiff > 5) {
            yaw = lerpAngleDegrees(prevBodyYaw, yaw, 0.1f)
        }

        if (world.isClient) {
            bigSpiderAnimationProperties.tick()
            legController.tick()
        }
    }

    override fun createBodyControl() = ArachneBodyControl(this)

    companion object {
        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
    }
}