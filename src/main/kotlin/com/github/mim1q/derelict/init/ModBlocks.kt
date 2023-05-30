package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.block.EmbersBlock
import com.github.mim1q.derelict.block.FancyCobwebBlock
import com.github.mim1q.derelict.block.SmolderingLeavesBlock
import com.github.mim1q.derelict.featureset.CoverBoardsSet
import com.github.mim1q.derelict.featureset.GrassSet
import com.github.mim1q.derelict.featureset.WoodSet
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object ModBlocks {
  val BURNED_WOOD = WoodSet(Derelict.id("burned"), defaultItemSettings()).register()
  val BURNED_LEAVES = register("burned_leaves", LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
  val SMOLDERING_LEAVES = register("smoldering_leaves", SmolderingLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
  val SMOKING_EMBERS = register("smoking_embers", EmbersBlock.Smoking(FabricBlockSettings.of(Material.FIRE)))
  val SMOLDERING_EMBERS = register("smoldering_embers", EmbersBlock.Smoldering(
    FabricBlockSettings.of(Material.FIRE).luminance(4).emissiveLighting {_, _, _ -> true}
  ))
  val DRIED_GRASS = GrassSet(Derelict.id("dried"), defaultItemSettings())
  val BURNED_GRASS = GrassSet(Derelict.id("burned"), defaultItemSettings())

  val OAK_COVER_BOARDS = CoverBoardsSet(Derelict.id("oak"), defaultItemSettings())
  val SPRUCE_COVER_BOARDS = CoverBoardsSet(Derelict.id("spruce"), defaultItemSettings())
  val BIRCH_COVER_BOARDS = CoverBoardsSet(Derelict.id("birch"), defaultItemSettings())
  val JUNGLE_COVER_BOARDS = CoverBoardsSet(Derelict.id("jungle"), defaultItemSettings())
  val ACACIA_COVER_BOARDS = CoverBoardsSet(Derelict.id("acacia"), defaultItemSettings())
  val DARK_OAK_COVER_BOARDS = CoverBoardsSet(Derelict.id("dark_oak"), defaultItemSettings())
  val MANGROVE_COVER_BOARDS = CoverBoardsSet(Derelict.id("mangrove"), defaultItemSettings())
  val WARPED_COVER_BOARDS = CoverBoardsSet(Derelict.id("warped"), defaultItemSettings())
  val CRIMSON_COVER_BOARDS = CoverBoardsSet(Derelict.id("crimson"), defaultItemSettings())
  val BURNED_COVER_BOARDS = CoverBoardsSet(Derelict.id("burned"), defaultItemSettings())

  val FLICKERING_REDSTONE_LAMP = register("flickering_redstone_lamp", Block(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP).luminance { 10 }))
  val FLICKERING_SEA_LANTERN = register("flickering_sea_lantern", Block(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN).luminance { 10 }))
  val BROKEN_SEA_LANTERN = register("broken_sea_lantern", Block(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN).luminance { 0 }))
  val FLICKERING_JACK_O_LANTERN = register("flickering_jack_o_lantern", CarvedPumpkinBlock(FabricBlockSettings.copyOf(Blocks.JACK_O_LANTERN).luminance { 10 }))
  val FLICKERING_LANTERN = register("flickering_lantern", LanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance { 10 }))
  val BROKEN_LANTERN = register("broken_lantern", LanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance { 0 }))
  val FLICKERING_SOUL_LANTERN = register("flickering_soul_lantern", LanternBlock(FabricBlockSettings.copyOf(Blocks.SOUL_LANTERN).luminance { 10 }))

  val FANCY_COBWEB = register("fancy_cobweb", FancyCobwebBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))

  fun init() { }

  private fun <T : Block> register(name: String, block: T): T {
    Registry.register(Registry.BLOCK, Derelict.id(name), block)
    Registry.register(Registry.ITEM, Derelict.id(name), BlockItem(block, FabricItemSettings().group(Derelict.ITEM_GROUP)))
    return block
  }

  private fun <T: Block> registerBlock(name: String, block: T): T = Registry.register(
    Registry.BLOCK, Derelict.id(name), block
  )

  private fun <T: Item> registerItem(name: String, item: T): T = Registry.register(
    Registry.ITEM, Derelict.id(name), item
  )

  private fun defaultItemSettings() = FabricItemSettings().group(Derelict.ITEM_GROUP)
}