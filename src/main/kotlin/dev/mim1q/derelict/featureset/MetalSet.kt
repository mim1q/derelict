package dev.mim1q.derelict.featureset

import dev.mim1q.derelict.block.RotatableCoverBlock
import dev.mim1q.derelict.block.RotatableCoverBlock.Normal.OxidizableNormal
import dev.mim1q.derelict.block.RotatableCoverBlock.SquarePatch
import dev.mim1q.derelict.block.RotatableCoverBlock.SquarePatch.OxidizableSquarePatch
import dev.mim1q.derelict.block.metal.BarbedWireBlock
import dev.mim1q.derelict.block.metal.BeamBlock
import dev.mim1q.derelict.block.metal.GrateBlock
import dev.mim1q.derelict.block.metal.MetalLadderBlock
import dev.mim1q.derelict.block.metal.oxidizable.*
import dev.mim1q.derelict.init.ModBlocksAndItems.ItemCategory
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.block.*
import net.minecraft.block.Oxidizable.OxidationLevel
import net.minecraft.util.Identifier

sealed class MetalSet(
  id: Identifier,
  defaultItemSettings: FabricItemSettings
) : FeatureSet(id, defaultItemSettings, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)) {
  abstract val block: Block
  abstract val cut: Block
  abstract val pillar: PillarBlock
  abstract val stairs: StairsBlock
  abstract val slab: SlabBlock
  abstract val chain: ChainBlock
  abstract val grate: GrateBlock
  abstract val beam: BeamBlock
  abstract val beamPile: BeamBlock
  abstract val ladder: LadderBlock
  abstract val patch: SquarePatch
  abstract val sheet: RotatableCoverBlock.Normal
  abstract val chainLinkFence: PaneBlock
  abstract val barbedWire: BarbedWireBlock

  fun getCutoutBlocks() = arrayOf(
    chain, grate, ladder, chainLinkFence, barbedWire
  )

  class Regular(
    id: Identifier,
    defaultItemSettings: FabricItemSettings,
    prefix: String = ""
  ) : MetalSet(id, defaultItemSettings) {
    override val block: Block = registerBlockWithItem("$prefix${name}_block", Block(defaultBlockSettings), ItemCategory.WAXED_METAL_BLOCKS)
    override val cut: Block = registerBlockWithItem("${prefix}cut_$name", Block(defaultBlockSettings), ItemCategory.WAXED_METAL_BLOCKS)
    override val pillar: PillarBlock = registerBlockWithItem("$prefix${name}_pillar", PillarBlock(defaultBlockSettings), ItemCategory.WAXED_METAL_BLOCKS)
    override val stairs: StairsBlock = registerBlockWithItem("${prefix}cut_${name}_stairs", StairsBlock(block.defaultState, defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_BLOCKS)
    override val slab: SlabBlock = registerBlockWithItem("${prefix}cut_${name}_slab", SlabBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_BLOCKS)
    override val chain: ChainBlock = registerBlockWithItem("$prefix${name}_chain", ChainBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val grate: GrateBlock = registerBlockWithItem("$prefix${name}_grate", GrateBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val beam: BeamBlock = registerBlockWithItem("$prefix${name}_beam", BeamBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val beamPile: BeamBlock = registerBlockWithItem("$prefix${name}_beam_pile", BeamBlock(defaultBlockSettings.nonOpaque(), true), ItemCategory.WAXED_METAL_DECORATION)
    override val ladder: LadderBlock = registerBlockWithItem("$prefix${name}_ladder", MetalLadderBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val patch: SquarePatch = registerBlockWithItem("$prefix${name}_patch", SquarePatch(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val sheet: RotatableCoverBlock.Normal = registerBlockWithItem("$prefix${name}_sheet", RotatableCoverBlock.Normal(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val chainLinkFence: PaneBlock = registerBlockWithItem("$prefix${name}_chain_link_fence", PaneBlock(defaultBlockSettings.nonOpaque()), ItemCategory.WAXED_METAL_DECORATION)
    override val barbedWire: BarbedWireBlock = registerBlockWithItem("$prefix${name}_barbed_wire", BarbedWireBlock(defaultBlockSettings.nonOpaque().noCollision()), ItemCategory.WAXED_METAL_DECORATION)
  }

  class Oxidized internal constructor(
    id: Identifier,
    prefix: String,
    defaultItemSettings: FabricItemSettings,
    level: OxidationLevel
  ) : MetalSet(id, defaultItemSettings) {
    override val block: Block = registerBlockWithItem("$prefix${name}_block", OxidizableBlock(level, defaultBlockSettings), ItemCategory.METAL_BLOCKS)
    override val cut: Block = registerBlockWithItem("${prefix}cut_$name", OxidizableBlock(level, defaultBlockSettings), ItemCategory.METAL_BLOCKS)
    override val pillar: PillarBlock = registerBlockWithItem("$prefix${name}_pillar", OxidizablePillarBlock(level, defaultBlockSettings), ItemCategory.METAL_BLOCKS)
    override val stairs: StairsBlock = registerBlockWithItem("${prefix}cut_${name}_stairs", OxidizableStairsBlock(level, block.defaultState, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_BLOCKS)
    override val slab: SlabBlock = registerBlockWithItem("${prefix}cut_${name}_slab", OxidizableSlabBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_BLOCKS)
    override val chain: ChainBlock = registerBlockWithItem("$prefix${name}_chain", OxidizableChainBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val grate: GrateBlock = registerBlockWithItem("$prefix${name}_grate", OxidizableGrateBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val beam: BeamBlock = registerBlockWithItem("$prefix${name}_beam", OxidizableBeamBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val beamPile: BeamBlock = registerBlockWithItem("$prefix${name}_beam_pile", OxidizableBeamBlock(level, defaultBlockSettings.nonOpaque(), true), ItemCategory.METAL_DECORATION)
    override val ladder: LadderBlock = registerBlockWithItem("$prefix${name}_ladder", OxidizableMetalLadderBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val patch: SquarePatch = registerBlockWithItem("$prefix${name}_patch", OxidizableSquarePatch(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val sheet: RotatableCoverBlock.Normal = registerBlockWithItem("$prefix${name}_sheet", OxidizableNormal(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val chainLinkFence: PaneBlock = registerBlockWithItem("$prefix${name}_chain_link_fence", OxidizablePaneBlock(level, defaultBlockSettings.nonOpaque()), ItemCategory.METAL_DECORATION)
    override val barbedWire: BarbedWireBlock = registerBlockWithItem("$prefix${name}_barbed_wire", OxidizableBarbedWireBlock(level, defaultBlockSettings.nonOpaque().noCollision()), ItemCategory.METAL_DECORATION)

    private fun registerOxidizable(base: Block, moreOxidized: Block?, waxed: Block) {
      if (moreOxidized != null) {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(base, moreOxidized)
      }
      OxidizableBlocksRegistry.registerWaxableBlockPair(base, waxed)
    }

    fun register(moreOxidizedSet: MetalSet?, waxedSet: MetalSet): Oxidized {
      registerOxidizable(block, moreOxidizedSet?.block, waxedSet.block)
      registerOxidizable(cut, moreOxidizedSet?.cut, waxedSet.cut)
      registerOxidizable(pillar, moreOxidizedSet?.pillar, waxedSet.pillar)
      registerOxidizable(stairs, moreOxidizedSet?.stairs, waxedSet.stairs)
      registerOxidizable(slab, moreOxidizedSet?.slab, waxedSet.slab)
      registerOxidizable(chain, moreOxidizedSet?.chain, waxedSet.chain)
      registerOxidizable(grate, moreOxidizedSet?.grate, waxedSet.grate)
      registerOxidizable(beam, moreOxidizedSet?.beam, waxedSet.beam)
      registerOxidizable(beamPile, moreOxidizedSet?.beamPile, waxedSet.beamPile)
      registerOxidizable(ladder, moreOxidizedSet?.ladder, waxedSet.ladder)
      registerOxidizable(patch, moreOxidizedSet?.patch, waxedSet.patch)
      registerOxidizable(sheet, moreOxidizedSet?.sheet, waxedSet.sheet)
      registerOxidizable(chainLinkFence, moreOxidizedSet?.chainLinkFence, waxedSet.chainLinkFence)
      registerOxidizable(barbedWire, moreOxidizedSet?.barbedWire, waxedSet.barbedWire)
      return this
    }
  }

  class FullOxidizable(id: Identifier, defaultItemSettings: FabricItemSettings) : FeatureSet(id, defaultItemSettings) {
    val waxedUnaffected = Regular(id, defaultItemSettings, "waxed_")
    val waxedExposed = Regular(id, defaultItemSettings, "waxed_exposed_")
    val waxedWeathered = Regular(id, defaultItemSettings, "waxed_weathered_")
    val waxedOxidized = Regular(id, defaultItemSettings, "waxed_oxidized_")
    val unaffected = Oxidized(id, "", defaultItemSettings, OxidationLevel.UNAFFECTED)
    val exposed = Oxidized(id, "exposed_", defaultItemSettings, OxidationLevel.EXPOSED)
    val weathered = Oxidized(id, "weathered_", defaultItemSettings, OxidationLevel.WEATHERED)
    val oxidized = Oxidized(id, "oxidized_", defaultItemSettings, OxidationLevel.OXIDIZED)

    fun getCutoutBlocks() = unaffected.getCutoutBlocks() + waxedUnaffected.getCutoutBlocks() +
      exposed.getCutoutBlocks() + waxedExposed.getCutoutBlocks() +
      weathered.getCutoutBlocks() + waxedWeathered.getCutoutBlocks() +
      oxidized.getCutoutBlocks() + waxedOxidized.getCutoutBlocks()

    override fun register(): FullOxidizable {
      unaffected.register(exposed, waxedUnaffected)
      exposed.register(weathered, waxedExposed)
      weathered.register(oxidized, waxedWeathered)
      oxidized.register(null, waxedOxidized)
      return this
    }
  }
}