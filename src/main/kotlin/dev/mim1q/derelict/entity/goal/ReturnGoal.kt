package dev.mim1q.derelict.entity.goal

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.BlockPos

class ReturnGoal<T : MobEntity>(
    private val entity: T,
    private val predicate: () -> Boolean,
    private val posGetter: () -> BlockPos?
) : Goal() {
    init {
        controls.add(Control.MOVE)
    }

    override fun tick() {
        super.tick()

        val pos = posGetter()
        if (pos != null) {
            entity.navigation.startMovingTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0.7)
        }
    }

    override fun canStart(): Boolean = posGetter() != null && predicate()
}