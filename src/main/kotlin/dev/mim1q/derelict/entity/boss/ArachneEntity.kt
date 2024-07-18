package dev.mim1q.derelict.entity.boss

import dev.mim1q.derelict.entity.spider.legs.SpiderLegIKSolver
import dev.mim1q.derelict.util.extensions.degrees
import dev.mim1q.derelict.util.extensions.getLocallyOffsetPos
import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world), BigSpider {
    override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)

    val firstLegIk = SpiderLegIKSolver(22 / 16f, 30 / 16f)
    var firstLegTargetPos = pos

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

        if (world.isClient) {
            bigSpiderAnimationProperties.tick()

            val offset = Vec3d(0.6, 0.9, 1.0)

            val particlePos = getLocallyOffsetPos(offset)
            world.addParticle(
                ParticleTypes.CRIT,
                particlePos.x,
                particlePos.y,
                particlePos.z,
                0.0,
                0.0,
                0.0
            )
            world.addParticle(
                ParticleTypes.ENCHANTED_HIT,
                firstLegTargetPos.x,
                firstLegTargetPos.y,
                firstLegTargetPos.z,
                0.0,
                0.0,
                0.0
            )


            val legYaw = MathHelper.wrapDegrees(firstLegIk.getYaw(1f).degrees() - bodyYaw)
            if (this.pos.distanceTo(firstLegTargetPos) > 3.0 || legYaw < 270 && legYaw > 90) {
                firstLegTargetPos = getLocallyOffsetPos(Vec3d(2.0, 0.0, 1.0))
            }
            firstLegIk.convertToLocalAndSolve(this, offset, firstLegTargetPos)
        }
    }

    override fun createBodyControl() = ArachneBodyControl(this)

    companion object {
        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
    }
}