package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.block.EmbersBlock
import com.github.mim1q.derelict.block.SmolderingLeavesBlock
import com.github.mim1q.derelict.block.cobweb.FancyCobwebBlock
import com.github.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderBlock
import com.github.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderNestBlock
import com.github.mim1q.derelict.block.cobweb.FancyCornerCobwebBlock
import com.github.mim1q.derelict.block.flickering.FlickeringCarvedPumpkinBlock
import com.github.mim1q.derelict.block.flickering.FlickeringLanternBlock
import com.github.mim1q.derelict.block.flickering.FlickeringSolidBlock
import com.github.mim1q.derelict.featureset.CoverBoardsSet
import com.github.mim1q.derelict.featureset.GrassSet
import com.github.mim1q.derelict.featureset.MetalSet
import com.github.mim1q.derelict.featureset.WoodSet
import com.github.mim1q.derelict.item.StaffItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

@Suppress("UNUSED")
object ModBlocksAndItems {
  val AGING_STAFF = registerItem("aging_staff", StaffItem.Aging(defaultItemSettings()))
  val WAXING_STAFF = registerItem("waxing_staff", StaffItem.Waxing(defaultItemSettings()))

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

  val FLICKERING_REDSTONE_LAMP = register("flickering_redstone_lamp", FlickeringSolidBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP)))
  val FLICKERING_SEA_LANTERN = register("flickering_sea_lantern", FlickeringSolidBlock(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN)))
  val BROKEN_SEA_LANTERN = register("broken_sea_lantern", Block(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN).luminance { 0 }))
  val FLICKERING_JACK_O_LANTERN = register("flickering_jack_o_lantern", FlickeringCarvedPumpkinBlock(FabricBlockSettings.copyOf(Blocks.JACK_O_LANTERN)))
  val FLICKERING_LANTERN = register("flickering_lantern", FlickeringLanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN)))
  val BROKEN_LANTERN = register("broken_lantern", LanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance { 0 }))
  val FLICKERING_SOUL_LANTERN = register("flickering_soul_lantern", FlickeringLanternBlock(FabricBlockSettings.copyOf(Blocks.SOUL_LANTERN)))

  val FANCY_COBWEB = register("fancy_cobweb", FancyCobwebBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))
  val FANCY_COBWEB_WITH_SPIDER_NEST = register("fancy_cobweb_with_spider_nest", FancyCobwebWithSpiderNestBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))
  val FANCY_COBWEB_WITH_SPIDER = register("fancy_cobweb_with_spider", FancyCobwebWithSpiderBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))
  val FANCY_COBWEB_WITH_SHY_SPIDER = register("fancy_cobweb_with_shy_spider", FancyCobwebWithSpiderBlock(FabricBlockSettings.copyOf(Blocks.COBWEB), true))
  val CORNER_COBWEB = register("corner_cobweb", FancyCornerCobwebBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))
  val FANCY_CORNER_COBWEB = register("fancy_corner_cobweb", FancyCornerCobwebBlock(FabricBlockSettings.copyOf(Blocks.COBWEB)))

  val CONSTRUCTION_STEEL = MetalSet.FullOxidizable(Derelict.id("construction_steel"), defaultItemSettings()).register()

  fun init() { }

  internal fun <T : Block> register(name: String, block: T, category: ItemCategory = ItemCategory.GENERAL): T {
    registerBlock(name, block)
    registerItem(name, BlockItem(block, defaultItemSettings()), category)
    return block
  }

  internal fun <T: Block> registerBlock(name: String, block: T): T = Registry.register(
    Registry.BLOCK, Derelict.id(name), block
  )

  internal fun <T: Item> registerItem(name: String, item: T, category: ItemCategory = ItemCategory.GENERAL): T {
    category.add(item)
    return Registry.register(Registry.ITEM, Derelict.id(name), item)
  }

  private fun defaultItemSettings() = FabricItemSettings().group(Derelict.ITEM_GROUP)

  enum class ItemCategory {
    GENERAL,
    METAL_BLOCKS,
    METAL_DECORATION,
    WAXED_METAL_BLOCKS,
    WAXED_METAL_DECORATION;

    val items: MutableList<Item> = mutableListOf()
    fun add(item: Item) = items.add(item)
  }
}