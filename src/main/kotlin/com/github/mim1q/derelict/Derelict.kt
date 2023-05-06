package com.github.mim1q.derelict

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
  private const val MOD_ID = "derelict"
  val LOGGER: Logger = LogManager.getLogger(MOD_ID)

  override fun onInitialize() {
    LOGGER.info("Derelict initializing")
  }

  fun id(value: String) = Identifier(MOD_ID, value)
}