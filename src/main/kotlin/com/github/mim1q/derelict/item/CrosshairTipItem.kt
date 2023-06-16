package com.github.mim1q.derelict.item

import net.minecraft.block.Block
import net.minecraft.util.Identifier

interface CrosshairTipItem {
  fun shouldShowTip(block: Block?): Boolean
  fun getTipTexture(): Identifier
}