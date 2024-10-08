package dev.mim1q.derelict.init

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.entity.SpiderlingEntity
import dev.mim1q.derelict.entity.boss.ArachneEntity
import dev.mim1q.derelict.entity.projectile.SpiderEggProjectile
import dev.mim1q.derelict.entity.projectile.SpiderSilkBolaProjectile
import dev.mim1q.derelict.entity.spider.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.mob.SpiderEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object ModEntities {
    val ARACHNE = register("arachne", ::ArachneEntity, ArachneEntity.createArachneAttributes()
    ) { dimensions(EntityDimensions.fixed(2.5f, 1.9f)) }

    val SPIDERLING = register("spiderling", SpiderlingEntity::Enemy, SpiderlingEntity.createSpiderlingAttributes()
    ) { dimensions(EntityDimensions.fixed(0.7f, 0.7f)) }

    val SPIDERLING_ALLY = register("spiderling_ally", SpiderlingEntity::Ally, SpiderlingEntity.createSpiderlingAllyAttributes()
    ) { dimensions(EntityDimensions.fixed(0.7f, 0.7f)) }

    val CHARMING_SPIDER = register("charming_spider", ::CharmingSpiderEntity, CharmingSpiderEntity.createCharmingSpiderAttributes()
    ) { dimensions(EntityDimensions.fixed(0.8f, 0.8f)) }

    val WEB_CASTER = register("web_caster", ::WebCasterEntity, WebCasterEntity.createWebCasterAttributes()
    ) { dimensions(EntityDimensions.fixed(1.9f, 1.2f)) }

    val DADDY_LONG_LEGS = register("daddy_long_legs", ::DaddyLongLegsEntity, SpiderEntity.createSpiderAttributes()
    ) { dimensions(EntityDimensions.fixed(0.9f, 3.6f)) }

    val JUMPING_SPIDER = register("jumping_spider", ::JumpingSpiderEntity, SpiderEntity.createSpiderAttributes()
    ) { dimensions(EntityDimensions.fixed(1.6f, 0.9f)) }

    val SPINY_SPIDER = register("spiny_spider", ::SpinySpiderEntity, SpiderEntity.createSpiderAttributes()
    ) { dimensions(EntityDimensions.fixed(0.8f, 0.8f)) }

    val ARACHNE_EGG = register("arachne_egg", ::ArachneEggEntity
    ) { dimensions(EntityDimensions.fixed(1.5f, 2.25f)) }

    val SPIDER_SILK_BOLA = register("spider_silk_bola", ::SpiderSilkBolaProjectile
    ) { dimensions(EntityDimensions.fixed(1.0f, 1.0f)) }

    val SPIDER_EGG_PROJECTILE = register("spider_egg_projectile", ::SpiderEggProjectile
    ) { dimensions(EntityDimensions.fixed(0.5f, 0.5f)) }

    fun init() {  }

    val SPAWN_ON_GROUND = setOf(
        SPIDERLING, CHARMING_SPIDER, WEB_CASTER, DADDY_LONG_LEGS, JUMPING_SPIDER, SPINY_SPIDER
    )

    private fun <E : Entity> register(
        name: String,
        entityfactory: EntityType.EntityFactory<E>,
        builderSetup: FabricEntityTypeBuilder<E>.() -> Unit = { }
    ): EntityType<E> = Registry.register(
        Registries.ENTITY_TYPE,
        Derelict.id(name),
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, entityfactory).apply(builderSetup).build()
    )

    private fun <E : LivingEntity> register(
        name: String,
        entityfactory: EntityType.EntityFactory<E>,
        attributes: DefaultAttributeContainer.Builder = ZombieEntity.createHostileAttributes(),
        spawnGroup: SpawnGroup = SpawnGroup.MONSTER,
        builderSetup: FabricEntityTypeBuilder<E>.() -> Unit = { }
    ): EntityType<E> = Registry.register(
        Registries.ENTITY_TYPE,
        Derelict.id(name),
        FabricEntityTypeBuilder.create(spawnGroup, entityfactory).apply(builderSetup).build()
    ).also {
        FabricDefaultAttributeRegistry.register(it, attributes)
    }
}