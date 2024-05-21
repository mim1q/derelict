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
import dev.mim1q.derelict.init.noZFighting
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry
import net.minecraft.block.*
import net.minecraft.block.Oxidizable.OxidationLevel
import net.minecraft.util.Identifier

sealed class MetalSet(
    id: Identifier,
    defaultItemSettings: FabricItemSettings,
    defaultBlockSettings: FabricBlockSettings
) : FeatureSet(id, defaultItemSettings, defaultBlockSettings) {
    abstract val block: Block
    abstract val cut: Block
    abstract val pillar: PillarBlock
    abstract val stairs: StairsBlock
    abstract val slab: SlabBlock
    abstract val trapdoor: TrapdoorBlock
    abstract val chain: ChainBlock
    abstract val grate: GrateBlock
    abstract val beam: BeamBlock
    abstract val ladder: LadderBlock
    abstract val patch: SquarePatch
    abstract val sheet: RotatableCoverBlock.Normal
    abstract val chainLinkFence: PaneBlock
    abstract val barbedWire: BarbedWireBlock

    fun getCutoutBlocks() = arrayOf(
        chain, grate, ladder, chainLinkFence, barbedWire, trapdoor
    )

    class Regular(
        id: Identifier,
        defaultItemSettings: FabricItemSettings,
        defaultBlockSettings: FabricBlockSettings,
        prefix: String = ""
    ) : MetalSet(id, defaultItemSettings, defaultBlockSettings) {
        override val block: Block = registerBlockWithItem(
            "$prefix${name}_block",
            Block(defaultBlockSettings()),
            ItemCategory.WAXED_METAL_BLOCKS
        )
        override val cut: Block =
            registerBlockWithItem("${prefix}cut_$name", Block(defaultBlockSettings()), ItemCategory.WAXED_METAL_BLOCKS)
        override val pillar: PillarBlock = registerBlockWithItem(
            "$prefix${name}_pillar",
            PillarBlock(defaultBlockSettings()),
            ItemCategory.WAXED_METAL_BLOCKS
        )
        override val stairs: StairsBlock = registerBlockWithItem(
            "${prefix}cut_${name}_stairs",
            StairsBlock(block.defaultState, defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_BLOCKS
        )
        override val slab: SlabBlock = registerBlockWithItem(
            "${prefix}cut_${name}_slab",
            SlabBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_BLOCKS
        )
        override val trapdoor: TrapdoorBlock = registerBlockWithItem(
            "$prefix${name}_trapdoor",
            TrapdoorBlock(defaultBlockSettings().nonOpaque(), BlockSetType.IRON),
            ItemCategory.WAXED_METAL_BLOCKS
        )
        override val chain: ChainBlock = registerBlockWithItem(
            "$prefix${name}_chain",
            ChainBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val grate: GrateBlock = registerBlockWithItem(
            "$prefix${name}_grate",
            GrateBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val beam: BeamBlock = registerBlockWithItem(
            "$prefix${name}_beam",
            BeamBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val ladder: LadderBlock = registerBlockWithItem(
            "$prefix${name}_ladder",
            MetalLadderBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val patch: SquarePatch = registerBlockWithItem(
            "$prefix${name}_patch",
            SquarePatch(defaultBlockSettings().nonOpaque().noZFighting()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val sheet: RotatableCoverBlock.Normal = registerBlockWithItem(
            "$prefix${name}_sheet",
            RotatableCoverBlock.Normal(defaultBlockSettings().nonOpaque().noZFighting()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val chainLinkFence: PaneBlock = registerBlockWithItem(
            "$prefix${name}_chain_link_fence",
            PaneBlock(defaultBlockSettings().nonOpaque()),
            ItemCategory.WAXED_METAL_DECORATION
        )
        override val barbedWire: BarbedWireBlock = registerBlockWithItem(
            "$prefix${name}_barbed_wire",
            BarbedWireBlock(defaultBlockSettings().nonOpaque().noCollision()),
            ItemCategory.WAXED_METAL_DECORATION
        )
    }

    class Oxidized internal constructor(
        id: Identifier,
        prefix: String,
        defaultItemSettings: FabricItemSettings,
        defaultBlockSettings: FabricBlockSettings,
        level: OxidationLevel
    ) : MetalSet(id, defaultItemSettings, defaultBlockSettings) {
        override val block: Block = registerBlockWithItem(
            "$prefix${name}_block",
            OxidizableBlock(level, defaultBlockSettings()),
            ItemCategory.METAL_BLOCKS
        )
        override val cut: Block = registerBlockWithItem(
            "${prefix}cut_$name",
            OxidizableBlock(level, defaultBlockSettings()),
            ItemCategory.METAL_BLOCKS
        )
        override val pillar: PillarBlock = registerBlockWithItem(
            "$prefix${name}_pillar",
            OxidizablePillarBlock(level, defaultBlockSettings()),
            ItemCategory.METAL_BLOCKS
        )
        override val stairs: StairsBlock = registerBlockWithItem(
            "${prefix}cut_${name}_stairs",
            OxidizableStairsBlock(level, block.defaultState, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_BLOCKS
        )
        override val slab: SlabBlock = registerBlockWithItem(
            "${prefix}cut_${name}_slab",
            OxidizableSlabBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_BLOCKS
        )
        override val trapdoor: TrapdoorBlock = registerBlockWithItem(
            "$prefix${name}_trapdoor",
            OxidizableTrapdoorBlock(level, defaultBlockSettings().nonOpaque(), BlockSetType.IRON),
            ItemCategory.METAL_BLOCKS
        )
        override val chain: ChainBlock = registerBlockWithItem(
            "$prefix${name}_chain",
            OxidizableChainBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_DECORATION
        )
        override val grate: OxidizableGrateBlock = registerBlockWithItem(
            "$prefix${name}_grate",
            OxidizableGrateBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_DECORATION
        )
        override val beam: BeamBlock = registerBlockWithItem(
            "$prefix${name}_beam",
            OxidizableBeamBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_DECORATION
        )
        override val ladder: LadderBlock = registerBlockWithItem(
            "$prefix${name}_ladder",
            OxidizableMetalLadderBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_DECORATION
        )
        override val patch: SquarePatch = registerBlockWithItem(
            "$prefix${name}_patch",
            OxidizableSquarePatch(level, defaultBlockSettings().nonOpaque().noZFighting()),
            ItemCategory.METAL_DECORATION
        )
        override val sheet: RotatableCoverBlock.Normal = registerBlockWithItem(
            "$prefix${name}_sheet",
            OxidizableNormal(level, defaultBlockSettings().nonOpaque().noZFighting()),
            ItemCategory.METAL_DECORATION
        )
        override val chainLinkFence: PaneBlock = registerBlockWithItem(
            "$prefix${name}_chain_link_fence",
            OxidizablePaneBlock(level, defaultBlockSettings().nonOpaque()),
            ItemCategory.METAL_DECORATION
        )
        override val barbedWire: BarbedWireBlock = registerBlockWithItem(
            "$prefix${name}_barbed_wire",
            OxidizableBarbedWireBlock(level, defaultBlockSettings().nonOpaque().noCollision()),
            ItemCategory.METAL_DECORATION
        )

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
            registerOxidizable(trapdoor, moreOxidizedSet?.trapdoor, waxedSet.trapdoor)
            registerOxidizable(chain, moreOxidizedSet?.chain, waxedSet.chain)
            registerOxidizable(grate, moreOxidizedSet?.grate, waxedSet.grate)
            registerOxidizable(beam, moreOxidizedSet?.beam, waxedSet.beam)
            registerOxidizable(ladder, moreOxidizedSet?.ladder, waxedSet.ladder)
            registerOxidizable(patch, moreOxidizedSet?.patch, waxedSet.patch)
            registerOxidizable(sheet, moreOxidizedSet?.sheet, waxedSet.sheet)
            registerOxidizable(chainLinkFence, moreOxidizedSet?.chainLinkFence, waxedSet.chainLinkFence)
            registerOxidizable(barbedWire, moreOxidizedSet?.barbedWire, waxedSet.barbedWire)
            return this
        }
    }

    class ThreeLevelOxidizable(
        id: Identifier,
        defaultItemSettings: FabricItemSettings,
        defaultBlockSettings: FabricBlockSettings
    ) : FeatureSet(id, defaultItemSettings, defaultBlockSettings) {
        val waxedUnaffected = Regular(id, defaultItemSettings, defaultBlockSettings, "waxed_")
        val waxedWeathered = Regular(id, defaultItemSettings, defaultBlockSettings, "waxed_weathered_")
        val waxedOxidized = Regular(id, defaultItemSettings, defaultBlockSettings, "waxed_oxidized_")
        val unaffected = Oxidized(id, "", defaultItemSettings, defaultBlockSettings, OxidationLevel.UNAFFECTED)
        val weathered = Oxidized(id, "weathered_", defaultItemSettings, defaultBlockSettings, OxidationLevel.WEATHERED)
        val oxidized = Oxidized(id, "oxidized_", defaultItemSettings, defaultBlockSettings, OxidationLevel.OXIDIZED)

        fun getCutoutBlocks() =
            unaffected.getCutoutBlocks() + waxedUnaffected.getCutoutBlocks() +
                weathered.getCutoutBlocks() + waxedWeathered.getCutoutBlocks() +
                oxidized.getCutoutBlocks() + waxedOxidized.getCutoutBlocks()

        override fun register(): ThreeLevelOxidizable {
            unaffected.register(weathered, waxedUnaffected)
            weathered.register(oxidized, waxedWeathered)
            oxidized.register(null, waxedOxidized)
            return this
        }
    }
}