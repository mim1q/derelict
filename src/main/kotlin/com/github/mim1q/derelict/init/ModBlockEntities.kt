package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object ModBlockEntities {

  val FANCY_COBWEB_WITH_SPIDER: BlockEntityType<FancyCobwebWithSpiderBlockEntity> = register(
    "fancy_cobweb_with_spider",
    ::FancyCobwebWithSpiderBlockEntity,
    ModBlocks.FANCY_COBWEB_WITH_SPIDER,
    ModBlocks.FANCY_COBWEB_WITH_SHY_SPIDER
  )

  fun init() { }

  private fun <T : BlockEntity> register(
    id: String,
    factory: FabricBlockEntityTypeBuilder.Factory<T>,
    vararg blocks: Block
  ) = Registry.register(
    Registry.BLOCK_ENTITY_TYPE,
    Derelict.id(id),
    FabricBlockEntityTypeBuilder.create(factory).addBlocks(*blocks).build()
  )
}