package dev.mim1q.derelict

import dev.mim1q.derelict.config.DerelictConfig
import dev.mim1q.derelict.config.DerelictConfigs
import dev.mim1q.derelict.init.*
import dev.mim1q.derelict.init.component.ModCardinalComponents.updateClientSyncedEffects
import dev.mim1q.derelict.init.worldgen.ModFeatures
import io.wispforest.owo.itemgroup.OwoItemGroup
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
    const val MOD_ID = "derelict"
    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    val CONFIG: DerelictConfig = DerelictConfigs.CONFIG
    val ITEM_GROUP: OwoItemGroup = ModItemGroups.ITEM_GROUP

    override fun onInitialize() {
        LOGGER.info("Derelict initializing")

        CONFIG.save()

        // General content
        ModBlocksAndItems.init()
        ModBlockEntities.init()
        ModEntities.init()
        ModStatusEffects.init()

        // Effects
        ModParticles.init()
        ModSounds.init()

        // Worldgen
        ModFeatures.init()

        // Post init setup
        ModBlocksAndItems.setupWaxableAndAgeable()

        // Events
        setupEventListeners()
    }

    private fun setupEventListeners() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            handler.player.updateClientSyncedEffects()
        }
        ServerPlayerEvents.COPY_FROM.register { player, _, _ ->
            player.updateClientSyncedEffects()
        }
    }

    fun id(value: String) = Identifier(MOD_ID, value)
}