package com.github.mim1q.derelict.init

import com.github.mim1q.derelict.Derelict
import com.github.mim1q.derelict.entity.boss.ArachneEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.util.registry.Registry

object ModEntities {
  val ARACHNE = register("arachne", ::ArachneEntity, attributes = ArachneEntity.createArachneAttributes()) {
    dimensions(EntityDimensions.fixed(2.5F, 2.0F))
  }

  fun init() {}

  private fun <E : LivingEntity> register(
    name: String,
    entityFactory: EntityType.EntityFactory<E>,
    spawnGroup: SpawnGroup = SpawnGroup.MONSTER,
    attributes: DefaultAttributeContainer.Builder = ZombieEntity.createHostileAttributes(),
    builderSetup: FabricEntityTypeBuilder<E>.() -> Unit = { }
  ): EntityType<E> = Registry.register(
      Registry.ENTITY_TYPE,
      Derelict.id(name),
      FabricEntityTypeBuilder.create(spawnGroup, entityFactory).apply(builderSetup).build()
    ).also {
      FabricDefaultAttributeRegistry.register(it, attributes)
  }
}