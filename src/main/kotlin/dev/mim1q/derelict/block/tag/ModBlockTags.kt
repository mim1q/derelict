package dev.mim1q.derelict.block.tag

import dev.mim1q.derelict.Derelict
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey

object ModBlockTags {
  val PREVENT_Z_FIGHTING = of("prevent_z_fighting")
  val COBWEBS = of("cobwebs")

  private fun of(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, Derelict.id(id))
}