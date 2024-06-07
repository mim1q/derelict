package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {
    fun init() {}

    private fun <T : BlockEntity> register(
        id: String,
        factory: FabricBlockEntityTypeBuilder.Factory<T>,
        vararg blocks: Block
    ) = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Derelict.id(id),
        FabricBlockEntityTypeBuilder.create(factory).addBlocks(*blocks).build()
    )
}