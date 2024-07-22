package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.entity.spider.CharmingSpiderEntity
import dev.mim1q.derelict.entity.spider.DaddyLongLegsEntity
import dev.mim1q.derelict.entity.spider.JumpingSpiderEntity
import dev.mim1q.derelict.entity.spider.WebCasterEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModEntities {
    val ARACHNE = register("arachne", ::ArachneEntity, attributes = ArachneEntity.createArachneAttributes()) {
        dimensions(EntityDimensions.fixed(2.5f, 1.9f))
    }

    val SPIDERLING = register("spiderling", ::SpiderlingEntity, attributes = SpiderlingEntity.createSpiderlingAttributes()) {
        dimensions(EntityDimensions.fixed(0.7f, 0.7f))
    }

    val CHARMING_SPIDER = register("charming_spider", ::CharmingSpiderEntity, attributes = SpiderEntity.createSpiderAttributes()) {
        dimensions(EntityDimensions.fixed(0.8f, 0.8f))
    }

    val WEB_CASTER = register("web_caster", ::WebCasterEntity, attributes = SpiderEntity.createSpiderAttributes()) {
        dimensions(EntityDimensions.fixed(2.3f, 1.1f))
    }

    val DADDY_LONG_LEGS = register("daddy_long_legs", ::DaddyLongLegsEntity, attributes = SpiderEntity.createSpiderAttributes()) {
        dimensions(EntityDimensions.fixed(0.9f, 3.6f))
    }

    val JUMPING_SPIDER = register("jumping_spider", ::JumpingSpiderEntity, attributes = SpiderEntity.createSpiderAttributes()) {
        dimensions(EntityDimensions.fixed(1.6f, 0.9f))
    }

    fun init() {}

    private fun <E : LivingEntity> register(
        name: String,
        entityfactory: EntityType.EntityFactory<E>,
        spawnGroup: SpawnGroup = SpawnGroup.MONSTER,
        attributes: DefaultAttributeContainer.Builder = ZombieEntity.createHostileAttributes(),
        builderSetup: FabricEntityTypeBuilder<E>.() -> Unit = { }
    ): EntityType<E> = Registry.register(
        Registries.ENTITY_TYPE,
        Derelict.id(name),
        FabricEntityTypeBuilder.create(spawnGroup, entityfactory).apply(builderSetup).build()
    ).also {
        FabricDefaultAttributeRegistry.register(it, attributes)
    }
}