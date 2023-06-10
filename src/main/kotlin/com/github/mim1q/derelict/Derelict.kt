package com.github.mim1q.derelict

import com.github.mim1q.derelict.config.DerelictConfig
import com.github.mim1q.derelict.init.ModBlockEntities
import com.github.mim1q.derelict.init.ModBlocksAndItems
import com.github.mim1q.derelict.init.ModParticles
import com.github.mim1q.derelict.item.tag.ModItemTags
import io.wispforest.owo.itemgroup.Icon
import io.wispforest.owo.itemgroup.OwoItemGroup
import net.fabricmc.api.ModInitializer
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Derelict : ModInitializer {
  private const val MOD_ID = "derelict"
  val LOGGER: Logger = LogManager.getLogger(MOD_ID)
  val CONFIG: DerelictConfig = DerelictConfig.createAndLoad()

  override fun onInitialize() {
    LOGGER.info("Derelict initializing")
    ModBlocksAndItems.init()
    ModBlockEntities.init()
    ModParticles.init()
  }

  fun id(value: String) = Identifier(MOD_ID, value)

  private val ITEM_GROUP_TEXTURE = id("textures/gui/derelictitemtab.png")

  val ITEM_GROUP: OwoItemGroup = OwoItemGroup.builder(id("derelict")) {
    ModBlocksAndItems.FLICKERING_REDSTONE_LAMP.asItem().defaultStack
  }.customTexture(ITEM_GROUP_TEXTURE).build().apply {
    addTab(Icon.of(ModBlocksAndItems.FLICKERING_LANTERN), "general", ModItemTags.GENERAL_TAB, false)
    addTab(Icon.of(ModBlocksAndItems.CONSTRUCTION_STEEL.unaffected.block), "unwaxed_metals", ModItemTags.UNWAXED_METALS, false)
    addTab(Icon.of(Items.HONEYCOMB), "waxed_metals", ModItemTags.WAXED_METALS, false)
    initialize()
  }
}