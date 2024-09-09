package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.block.cobweb.SpiderEggBlock.SpiderEggBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {
    val SPIDER_EGG_BLOCK_ENTITY = register(
        "spider_egg", ::SpiderEggBlockEntity,
        ModBlocksAndItems.SPIDER_EGG, ModBlocksAndItems.SPIDER_EGG_CLUSTER,
        ModBlocksAndItems.SPIDERLING_SPIDER_EGG, ModBlocksAndItems.SPIDER_SPIDER_EGG,
        ModBlocksAndItems.CAVE_SPIDER_SPIDER_EGG, ModBlocksAndItems.JUMPING_SPIDER_SPIDER_EGG,
        ModBlocksAndItems.WEB_CASTER_SPIDER_EGG
    )

    fun init() {}

    private fun <T : BlockEntity> register(
        id: String,
        factory: FabricBlockEntityTypeBuilder.Factory<T>,
        vararg blocks: Block
    ): BlockEntityType<T> = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Derelict.id(id),
        FabricBlockEntityTypeBuilder.create(factory).addBlocks(*blocks).build()
    )
}