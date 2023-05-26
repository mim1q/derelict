package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModItems {
  fun init() { }

  private fun <T : Item> register(name: String, item: T): T = Registry.register(Registry.ITEM, Derelict.id(name), item)
  private fun defaultItemSettings() = FabricItemSettings().group(Derelict.ITEM_GROUP)
}