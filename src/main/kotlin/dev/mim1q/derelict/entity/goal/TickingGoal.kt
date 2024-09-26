package dev.mim1q.derelict.entity.goal

import net.minecraft.entity.ai.goal.Goal

open class TickingGoal(
    private val length: Int = 100,
    private val onTick: (Int) -> Unit = {},
    private val onStart: () -> Unit = {},
    private val onStop: () -> Unit = {},
    private val startPredicate: () -> Boolean = { true }
) : Goal() {
    private var ticks = 0

    override fun canStart(): Boolean = startPredicate()

    override fun start() {
        ticks = 0
        onStart()
    }

    override fun stop() {
        ticks = 0
        onStop()
    }

    override fun shouldContinue(): Boolean = ticks < length
    override fun tick() {
        ticks++
        onTick(ticks)
    }

    override fun shouldRunEveryTick(): Boolean = true
}