package com.github.mim1q.derelict.featureset

import com.github.mim1q.derelict.block.metal.*
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

  class Regular(
    id: Identifier,
    defaultItemSettings: FabricItemSettings,
    prefix: String = ""
  ) : MetalSet(id, defaultItemSettings) {
    override val block: Block = registerBlockWithItem("$prefix${name}_block", Block(defaultBlockSettings))
    override val cut: Block = registerBlockWithItem("${prefix}cut_$name", Block(defaultBlockSettings))
    override val pillar: PillarBlock = registerBlockWithItem("$prefix${name}_pillar", PillarBlock(defaultBlockSettings))
    override val stairs: StairsBlock = registerBlockWithItem("${prefix}cut_${name}_stairs", StairsBlock(block.defaultState, defaultBlockSettings.nonOpaque()))
    override val slab: SlabBlock = registerBlockWithItem("${prefix}cut_${name}_slab", SlabBlock(defaultBlockSettings.nonOpaque()))
    override val chain: ChainBlock = registerBlockWithItem("$prefix${name}_chain", ChainBlock(defaultBlockSettings.nonOpaque()))
    override val grate: GrateBlock = registerBlockWithItem("$prefix${name}_grate", GrateBlock(defaultBlockSettings.nonOpaque()))
    override val beam: BeamBlock = registerBlockWithItem("$prefix${name}_beam", BeamBlock(defaultBlockSettings.nonOpaque()))
    override val beamPile: BeamBlock = registerBlockWithItem("$prefix${name}_beam_pile", BeamBlock(defaultBlockSettings.nonOpaque(), true))
  }

  class Oxidized internal constructor(
    id: Identifier,
    prefix: String,
    defaultItemSettings: FabricItemSettings,
    level: OxidationLevel
  ) : MetalSet(id, defaultItemSettings) {
    override val block: Block = registerBlockWithItem("$prefix${name}_block", OxidizableBlock(level, defaultBlockSettings))
    override val cut: Block = registerBlockWithItem("${prefix}cut_$name", OxidizableBlock(level, defaultBlockSettings))
    override val pillar: PillarBlock = registerBlockWithItem("$prefix${name}_pillar", OxidizablePillarBlock(level, defaultBlockSettings))
    override val stairs: StairsBlock = registerBlockWithItem("${prefix}cut_${name}_stairs", OxidizableStairsBlock(level, block.defaultState, defaultBlockSettings.nonOpaque()))
    override val slab: SlabBlock = registerBlockWithItem("${prefix}cut_${name}_slab", OxidizableSlabBlock(level, defaultBlockSettings.nonOpaque()))
    override val chain: ChainBlock = registerBlockWithItem("$prefix${name}_chain", OxidizableChainBlock(level, defaultBlockSettings.nonOpaque()))
    override val grate: GrateBlock = registerBlockWithItem("$prefix${name}_grate", OxidizableGrateBlock(level, defaultBlockSettings.nonOpaque()))
    override val beam: BeamBlock = registerBlockWithItem("$prefix${name}_beam", OxidizableBeamBlock(level, defaultBlockSettings.nonOpaque()))
    override val beamPile: BeamBlock = registerBlockWithItem("$prefix${name}_beam_pile", OxidizableBeamBlock(level, defaultBlockSettings.nonOpaque(), true))

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

    fun getCutoutBlocks() = arrayOf(
      unaffected.grate, unaffected.chain, waxedUnaffected.grate, waxedUnaffected.chain,
      exposed.grate, exposed.chain, waxedExposed.grate, waxedExposed.chain,
      weathered.grate, weathered.chain, waxedWeathered.grate, waxedWeathered.chain,
      oxidized.grate, oxidized.chain, waxedOxidized.grate, waxedOxidized.chain
    )

    override fun register(): FullOxidizable {
      unaffected.register(exposed, waxedUnaffected)
      exposed.register(weathered, waxedExposed)
      weathered.register(oxidized, waxedWeathered)
      oxidized.register(null, waxedOxidized)
      return this
    }
  }
}