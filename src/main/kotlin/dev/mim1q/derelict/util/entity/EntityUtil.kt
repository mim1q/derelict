package dev.mim1q.derelict.util.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.data.TrackedData
import java.util.*
import kotlin.enums.EnumEntries

class TrackedDataDelegate<T>(private val data: TrackedData<T>) {
    operator fun getValue(entity: Entity, property: Any): T = entity.dataTracker.get(data)
    operator fun setValue(entity: Entity, property: Any, value: T) = entity.dataTracker.set(data, value)
}

class OptionalTrackedDataDelegate<T : Any>(private val data: TrackedData<Optional<T>>) {
    operator fun getValue(entity: Entity, property: Any): T? = entity.dataTracker.get(data).orElse(null)
    operator fun setValue(entity: Entity, property: Any, value: T?) = entity.dataTracker.set(data, Optional.ofNullable(value))
}

class TrackedDataIntAsEnumDelegate<T : Enum<T>>(private val data: TrackedData<Int>, private val values: Array<T>) {
    operator fun getValue(entity: Entity, property: Any): T = values.getOrElse(entity.dataTracker.get(data)) { values[0] }
    operator fun setValue(entity: Entity, property: Any, value: T) = entity.dataTracker.set(data, values.indexOf(value))
}

fun <T> tracked(data: TrackedData<T>): TrackedDataDelegate<T> = TrackedDataDelegate(data)
fun <T : Any> nullableTracked(data: TrackedData<Optional<T>>): OptionalTrackedDataDelegate<T> =
    OptionalTrackedDataDelegate(data)

inline fun <reified T : Enum<T>> trackedEnum(data: TrackedData<Int>, values: EnumEntries<T>): TrackedDataIntAsEnumDelegate<T> =
    TrackedDataIntAsEnumDelegate(data, values.toTypedArray())