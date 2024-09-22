package dev.mim1q.derelict.init

import dev.mim1q.derelict.entity.boss.ArachneEntity.ArachneAttackType
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry

object ModTrackedDataHandlers {

    val ARACHNE_ATTACK: TrackedDataHandler<ArachneAttackType> = registerEnum()

    private inline fun <reified T : Enum<T>> registerEnum() = register(TrackedDataHandler.ofEnum(T::class.java))

    private fun <T> register(handler: TrackedDataHandler<T>): TrackedDataHandler<T> = handler.also {
        TrackedDataHandlerRegistry.register(it)
    }

    fun init() {
    }
}