package dev.mim1q.derelict.featureset

import dev.mim1q.derelict.block.RotatableCoverBlock
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Blocks
import net.minecraft.util.Identifier

class CoverBoardsSet(
  id: Identifier,
  defaultItemSettings: FabricItemSettings,
  defaultBlockSettings: FabricBlockSettings = FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
) : FeatureSet(id, defaultItemSettings, defaultBlockSettings = defaultBlockSettings)  {
  val single = registerBlockWithItem("${name}_cover_board", RotatableCoverBlock.Normal(defaultBlockSettings))
  val double = registerBlockWithItem("double_${name}_cover_boards", RotatableCoverBlock.Normal(defaultBlockSettings))
  val crossed = registerBlockWithItem("crossed_${name}_cover_boards", RotatableCoverBlock.Crossed(defaultBlockSettings))

  override fun register(): CoverBoardsSet {
    return this
  }
}