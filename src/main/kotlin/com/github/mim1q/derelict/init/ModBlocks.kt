package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.block.EmbersBlock
import com.github.mim1q.derelict.block.SmolderingLeavesBlock
import com.github.mim1q.derelict.featureset.CoverBoardsSet
import com.github.mim1q.derelict.featureset.WoodSet
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.LeavesBlock
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object ModBlocks {
  val BURNED_WOOD = WoodSet(Derelict.id("burned"), defaultItemSettings()).register()
  val BURNED_LEAVES = register("burned_leaves", LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
  val SMOLDERING_LEAVES = register("smoldering_leaves", SmolderingLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)))
  val SMOLDERING_EMBERS = register("smoldering_embers", EmbersBlock.Smoldering(
    FabricBlockSettings.of(Material.FIRE).luminance(4).emissiveLighting {_, _, _ -> true}
  ))
  val SMOKING_EMBERS = register("smoking_embers", EmbersBlock.Smoking(FabricBlockSettings.of(Material.FIRE)))

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