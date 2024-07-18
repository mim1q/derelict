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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ArachneEntity(entityType: EntityType<ArachneEntity>, world: World) : HostileEntity(entityType, world), BigSpider {
    override val bigSpiderAnimationProperties = BigSpiderAnimationProperties(this)

    val firstLegIk = SpiderLegIKSolver(22 / 16f, 30 / 16f)
    var prevLegTargetPos: Vec3d = pos
    var firstLegTargetPos = pos
    var firstLegNextTargetPos: Vec3d? = pos
    var legTargetTicks = 0

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

            val shouldMove = age % 20 == 0

            if (firstLegNextTargetPos == null && shouldMove) {
                prevLegTargetPos = firstLegTargetPos
                firstLegNextTargetPos = findLegPosition(getLocallyOffsetPos(Vec3d(2.0, 0.0, 3.0)))
            }

            if (firstLegNextTargetPos != null) {

                if (firstLegTargetPos.squaredDistanceTo(firstLegNextTargetPos) > 0.1) {
                    firstLegTargetPos = prevLegTargetPos.lerp(firstLegNextTargetPos, legTargetTicks / 10.0).add(
                        0.0,
                        0.05 * legTargetTicks * (10 - legTargetTicks),
                        0.0
                    )
                }

                legTargetTicks++
                if (legTargetTicks > 10) {
                    firstLegNextTargetPos = null
                }
            } else {
                legTargetTicks = 0
            }

            firstLegIk.convertToLocalAndSolve(this, offset, firstLegTargetPos)
        }
    }

    private fun findLegPosition(base: Vec3d): Vec3d {
        for (i in 1 downTo -2) {
            val pos = BlockPos.ofFloored(base).up(i)
            val block = world.getBlockState(pos)
            val shape = block.getOutlineShape(world, pos)
            println(block.block)

            if (!shape.isEmpty) {
                return Vec3d(base.x, pos.y + shape.getMax(Direction.Axis.Y), base.z)
            }
        }
        return base
    }

    override fun createBodyControl() = ArachneBodyControl(this)

    companion object {
        fun createArachneAttributes(): DefaultAttributeContainer.Builder = createHostileAttributes()
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6)
    }
}