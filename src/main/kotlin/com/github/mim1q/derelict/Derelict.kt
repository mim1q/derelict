package com.github.mim1q.derelict

import com.github.mim1q.derelict.init.ModBlocks
import com.github.mim1q.derelict.init.ModItems
import com.github.mim1q.derelict.init.ModParticles
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
  private const val MOD_ID = "derelict"
  val LOGGER: Logger = LogManager.getLogger(MOD_ID)

  override fun onInitialize() {
    LOGGER.info("Derelict initializing")
    ModBlocks.init()
    ModItems.init()
    ModParticles.init()
  }

  fun id(value: String) = Identifier(MOD_ID, value)

  val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.create(id("derelict"))
    .icon { ModBlocks.BURNED_WOOD.log.asItem().defaultStack }
    .build()
}