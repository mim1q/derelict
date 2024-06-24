package dev.mim1q.derelict

import dev.mim1q.derelict.config.DerelictConfig
import dev.mim1q.derelict.config.DerelictConfigs
import dev.mim1q.derelict.init.*
import io.wispforest.owo.itemgroup.OwoItemGroup
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
    private const val MOD_ID = "derelict"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    val CONFIG: DerelictConfig = DerelictConfigs.CONFIG
    val ITEM_GROUP: OwoItemGroup = ModItemGroups.ITEM_GROUP

    override fun onInitialize() {
        LOGGER.info("Derelict initializing")

        CONFIG.save()

        ModBlocksAndItems.init()
        ModBlockEntities.init()
        ModParticles.init()
        ModEntities.init()
        ModStatusEffects.init()

        ModBlocksAndItems.setupWaxableAndAgeable()
    }

    fun id(value: String) = Identifier(MOD_ID, value)
}