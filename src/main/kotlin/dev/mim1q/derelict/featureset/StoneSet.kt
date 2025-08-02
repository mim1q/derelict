package dev.mim1q.derelict.featureset

import net.fabricmc.fabric.api.`object`.builder.v1.block.type.BlockSetTypeBuilder
import net.minecraft.block.*
import net.minecraft.util.Identifier

class StoneSet(
    id: Identifier,
    defaultBlockSettings: AbstractBlock.Settings = AbstractBlock.Settings.copy(Blocks.STONE)
) : FeatureSet(id, defaultBlockSettings = defaultBlockSettings) {
    val blockSetType = BlockSetTypeBuilder.copyOf(BlockSetType.STONE).build(id)

    val block = Block(defaultBlockSettings)
    val stairs = StairsBlock(block.defaultState, defaultBlockSettings)
    val slab = SlabBlock(defaultBlockSettings)
    val wall = WallBlock(defaultBlockSettings)
    val pressurePlate = PressurePlateBlock(blockSetType, defaultBlockSettings)
    val button = ButtonBlock(blockSetType, 5, defaultBlockSettings)

    override fun register(): StoneSet = this.apply {
        registerBlockWithItem(name, block)
        registerBlockWithItem("${name}_stairs", stairs)
        registerBlockWithItem("${name}_slab", slab)
        registerBlockWithItem("${name}_wall", wall)
        registerBlockWithItem("${name}_pressure_plate", pressurePlate)
        registerBlockWithItem("${name}_button", button)
    }
}