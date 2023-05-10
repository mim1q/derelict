package com.github.mim1q.derelict.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Blocks
import net.minecraft.block.LichenGrower
import net.minecraft.block.MultifaceGrowthBlock

class SmolderingEmbersBlock : MultifaceGrowthBlock(FabricBlockSettings.copyOf(Blocks.FIRE)) {
  override fun getGrower(): LichenGrower = null!!
}