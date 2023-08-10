package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.block.cobweb.FancyCobwebWithSpiderBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModBlockEntities {

  val FANCY_COBWEB_WITH_SPIDER: BlockEntityType<FancyCobwebWithSpiderBlockEntity> = register(
    "fancy_cobweb_with_spider",
    ::FancyCobwebWithSpiderBlockEntity,
    ModBlocksAndItems.FANCY_COBWEB_WITH_SPIDER,
    ModBlocksAndItems.FANCY_COBWEB_WITH_SHY_SPIDER
  )

  fun init() { }

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