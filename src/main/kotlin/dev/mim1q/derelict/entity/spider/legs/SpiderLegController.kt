package dev.mim1q.derelict.entity.spider.legs

import dev.mim1q.derelict.util.extensions.getLocallyOffsetPos
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class SpiderLegController(
    private val entity: Entity,
    private val upperLimbLength: Float,
    private val lowerLimbLength: Float,
    vararg offsetsAndTargets: Pair<Vec3d, Vec3d>
) {
    init {
        assert(offsetsAndTargets.size == 8)
    }

    private val legs = offsetsAndTargets.mapIndexed { index, it ->
        SingleLegController(it.first, it.second, index >= 4)
    }.toTypedArray()

    fun tick() {
        legs.forEachIndexed { index, leg ->
            val offset = if ((index % 2 == 0) xor (index >= 4)) 5 else 0
            val isTimeToMove = entity.age % 10 == (offset)
            leg.step(isTimeToMove)

            // FOR DEBUGGING PURPOSES

//            val particlePos = entity.getLocallyOffsetPos(leg.offset)
//
//            entity.world.addParticle(
//                ParticleTypes.CRIT,
//                particlePos.x, particlePos.y, particlePos.z,
//                0.0, 0.0, 0.0
//            )
//            entity.world.addParticle(
//                ParticleTypes.ENCHANTED_HIT,
//                leg.currentTargetPos.x, leg.currentTargetPos.y, leg.currentTargetPos.z,
//                0.0, 0.0, 0.0
//            )
        }
    }

    fun getIk(index: Int) = legs[index].ikSolver

    private inner class SingleLegController(
        val offset: Vec3d,
        val target: Vec3d,
        val right: Boolean,
    ) {
        val ikSolver = SpiderLegIKSolver(upperLimbLength, lowerLimbLength)

        var prevTargetPos: Vec3d = entity.pos
        var currentTargetPos: Vec3d = entity.pos
        var nextTargetPos: Vec3d? = entity.pos

        var targetChangeTicks: Int = 0

        fun step(shouldMove: Boolean) {
            if (shouldMove && nextTargetPos == null) {
                prevTargetPos = currentTargetPos
                nextTargetPos = findLegPosition(entity.getLocallyOffsetPos(target))
            }

            val transitionTime = 5.0

            if (nextTargetPos != null) {
                if (currentTargetPos.squaredDistanceTo(nextTargetPos) > 0.5) {
                    currentTargetPos = prevTargetPos.lerp(nextTargetPos, targetChangeTicks / transitionTime).add(
                        0.0,
                        0.05 * targetChangeTicks * (transitionTime - targetChangeTicks),
                        0.0
                    )
                }

                targetChangeTicks++
                if (targetChangeTicks > transitionTime) {
                    nextTargetPos = null
                }
            } else {
                targetChangeTicks = 0
            }

            ikSolver.convertToLocalAndSolve(entity, offset, currentTargetPos, true, 4.0)
        }

        private fun findLegPosition(base: Vec3d): Vec3d {
            for (i in 1 downTo -2) {
                val pos = BlockPos.ofFloored(base).up(i)
                val block = entity.world.getBlockState(pos)
                val shape = block.getCollisionShape(entity.world, pos)
                if (!shape.isEmpty) {
                    return Vec3d(base.x, pos.y + shape.getMax(Direction.Axis.Y), base.z)
                }
            }
            return base.subtract(0.0, 1.0, 0.0)
        }
    }
}