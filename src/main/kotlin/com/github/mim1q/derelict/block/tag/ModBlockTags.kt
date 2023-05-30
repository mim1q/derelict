package com.github.mim1q.derelict.block.tag

import com.github.mim1q.derelict.Derelict
import net.minecraft.block.Block
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry

object ModBlockTags {
  val COVER_BOARDS = of("cover_boards")
  val COBWEBS = of("cobwebs")

  private fun of(id: String): TagKey<Block> = TagKey.of(Registry.BLOCK_KEY, Derelict.id(id))
}