package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.item.graffiti.SprayCanItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModItems {
  val SPRAY_CAN = register("spray_can", SprayCanItem(graffitiItemSettings()))
  val RAINBOW_SPRAY_CAN = register("rainbow_spray_can", SprayCanItem(graffitiItemSettings()))


  fun init() { }

  private fun <T : Item> register(name: String, item: T) = Registry.register(Registry.ITEM, Derelict.id(name), item)
  private fun defaultItemSettings() = FabricItemSettings().group(Derelict.ITEM_GROUP)
  private fun graffitiItemSettings() = defaultItemSettings().group(Derelict.GRAFFITI_ITEM_GROUP)
}