package dev.mim1q.derelict.tag

import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModBlockTags {
    val COBWEBS = of("cobwebs")
    val FULL_COBWEBS = of("full_cobwebs")

    private fun of(id: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, Identifier("derelict", id))
}