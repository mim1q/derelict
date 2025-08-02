package dev.mim1q.derelict.featureset

import dev.mim1q.derelict.block.RotatableCoverBlock
import dev.mim1q.derelict.init.noZFighting
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.util.Identifier

class CoverBoardsSet(
    id: Identifier,
    defaultItemSettings: Item.Settings,
    defaultBlockSettings: Settings = Settings.copy(Blocks.OAK_PLANKS).noZFighting()
) : FeatureSet(id, defaultItemSettings, defaultBlockSettings = defaultBlockSettings) {
    val single = registerBlockWithItem("${name}_cover_board", RotatableCoverBlock.Normal(defaultBlockSettings))
    val double = registerBlockWithItem("double_${name}_cover_boards", RotatableCoverBlock.Normal(defaultBlockSettings))
    val crossed =
        registerBlockWithItem("crossed_${name}_cover_boards", RotatableCoverBlock.Crossed(defaultBlockSettings))

    override fun register(): CoverBoardsSet {
        return this
    }
}