package dev.mim1q.derelict.util.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.data.TrackedData
import java.util.*

class TrackedDataDelegate<T>(private val data: TrackedData<T>) {
    operator fun getValue(entity: Entity, property: Any): T = entity.dataTracker.get(data)
    operator fun setValue(entity: Entity, property: Any, value: T) = entity.dataTracker.set(data, value)
}

class OptionalTrackedDataDelegate<T : Any>(private val data: TrackedData<Optional<T>>) {
    operator fun getValue(entity: Entity, property: Any): T? = entity.dataTracker.get(data).orElse(null)
    operator fun setValue(entity: Entity, property: Any, value: T?) = entity.dataTracker.set(data, Optional.ofNullable(value))
}

fun <T> Entity.tracked(data: TrackedData<T>): TrackedDataDelegate<T> = TrackedDataDelegate(data)
fun <T : Any> Entity.nullableTracked(data: TrackedData<Optional<T>>): OptionalTrackedDataDelegate<T> = OptionalTrackedDataDelegate(data)