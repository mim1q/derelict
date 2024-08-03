package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent

object ModSounds {
    val SPIDER_CAVES_AMBIENT = register("ambient.spider_caves")

    fun init() = Unit

    fun register(name: String): SoundEvent = Registry.register(
        Registries.SOUND_EVENT,
        Derelict.id(name),
        SoundEvent.of(Derelict.id(name))
    )
}