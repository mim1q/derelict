package dev.mim1q.derelict.util

import net.minecraft.util.StringIdentifiable

@JvmInline
value class StringWrapper(val value: String) : StringIdentifiable {
    override fun asString() = value
}