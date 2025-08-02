@file:Suppress("DEPRECATION")

package dev.mim1q.derelict.util.extensions

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

fun ItemStack.getNbt(): NbtCompound {
    val component = get(DataComponentTypes.CUSTOM_DATA)
    if (component != null) {
        val nbt = component.copyNbt()
        if (nbt != null) {
            return nbt
        }
    }
    return NbtCompound()
}

fun ItemStack.setNbt(nbt: NbtCompound) {
    set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt))
}

fun ItemStack.updateNbt(updater: NbtCompound.() -> Unit) {
    val nbt = getNbt()
    updater(nbt)
    setNbt(nbt)
}

fun Settings.copy(): Settings = FabricBlockSettings.copyOf(this)