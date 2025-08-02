package dev.mim1q.derelict.featureset

import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.ShortPlantBlock
import net.minecraft.block.TallPlantBlock
import net.minecraft.item.Item
import net.minecraft.util.Identifier

class GrassSet(
    id: Identifier,
    defaultItemSettings: Item.Settings
) : FeatureSet(id, defaultItemSettings) {
    val grassBlock = registerBlockWithItem("${name}_grass_block", Block(Settings.copy(Blocks.GRASS_BLOCK)))
    val grass = registerBlockWithItem("${name}_grass", ShortPlantBlock(Settings.copy(Blocks.SHORT_GRASS)))
    val tallGrass =
        registerBlockWithItem("tall_${name}_grass", TallPlantBlock(Settings.copy(Blocks.SHORT_GRASS)))

    override fun register(): FeatureSet = this
}