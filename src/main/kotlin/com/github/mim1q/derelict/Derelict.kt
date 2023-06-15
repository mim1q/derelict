package com.github.mim1q.derelict

import com.github.mim1q.derelict.config.DerelictClientConfig
import com.github.mim1q.derelict.config.DerelictConfig
import com.github.mim1q.derelict.init.ModBlockEntities
import com.github.mim1q.derelict.init.ModBlocksAndItems
import com.github.mim1q.derelict.init.ModItemGroups
import com.github.mim1q.derelict.init.ModParticles
import io.wispforest.owo.itemgroup.OwoItemGroup
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
  private const val MOD_ID = "derelict"
  val LOGGER: Logger = LogManager.getLogger(MOD_ID)
  val CONFIG: DerelictConfig = DerelictConfig.createAndLoad()
  val CLIENT_CONFIG: DerelictClientConfig = DerelictClientConfig.createAndLoad()
  val ITEM_GROUP: OwoItemGroup = ModItemGroups.ITEM_GROUP

  override fun onInitialize() {
    LOGGER.info("Derelict initializing")
    ModBlocksAndItems.init()
    ModBlockEntities.init()
    ModParticles.init()
  }
  fun id(value: String) = Identifier(MOD_ID, value)
}