package dev.mim1q.derelict.init.client

import dev.mim1q.derelict.Derelict
import dev.mim1q.derelict.client.render.armor.NetwalkersRenderer
import dev.mim1q.derelict.client.render.block.SpiderEggClusterRenderer
import dev.mim1q.derelict.client.render.entity.boss.arachne.ArachneEntityRenderer
import dev.mim1q.derelict.client.render.entity.boss.arachne.ArachneTexturedModelData
import dev.mim1q.derelict.client.render.entity.nonliving.HangingCocoonEntityRenderer
import dev.mim1q.derelict.client.render.entity.projectile.SpiderEggProjectileRenderer
import dev.mim1q.derelict.client.render.entity.projectile.SpiderSilkBolaRenderer
import dev.mim1q.derelict.client.render.entity.spider.*
import dev.mim1q.derelict.init.ModBlockEntities
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModEntities
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayer

object ModRender {
    private var customRenderersWorkaroundInit = false

    val ARACHNE_LAYER = registerLayer(ArachneTexturedModelData::create, "arachne")

    val SPIDERLING_LAYER = registerLayer(::getSpiderlingTexturedModelData, "spiderling")
    val CHARMING_SPIDER_LAYER = registerLayer(CharmingSpiderEntityModel::createTexturedModelData, "charming_spider")
    val WEB_CASTER_LAYER = registerLayer(WebCasterEntityModel::createTexturedModelData, "web_caster")
    val DADDY_LONG_LEGS_LAYER = registerLayer(DaddyLongLegsEntityModel::createTexturedModelData, "daddy_long_legs")
    val JUMPING_SPIDER_LAYER = registerLayer(JumpingSpiderEntityModel::createTexturedModelData, "jumping_spider")
    val SPINY_SPIDER_LAYER = registerLayer(SpinySpiderEntityModel::createTexturedModelData, "spiny_spider")

    val HANGING_COCOON_LAYER = registerLayer(HangingCocoonEntityRenderer::createTexturedModelData, "hanging_cocoon")

    val NETWALKERS_LAYER = registerLayer(NetwalkersRenderer::createTexturedModelData, "netwalkers")

    fun init() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutout(),
            ModBlocksAndItems.BURNED_WOOD.trapdoor,
            ModBlocksAndItems.BURNED_WOOD.door,
            ModBlocksAndItems.BURNED_LEAVES,
            ModBlocksAndItems.SMOLDERING_LEAVES,
            ModBlocksAndItems.BURNED_GRASS.grassBlock,
            ModBlocksAndItems.BURNED_GRASS.grass,
            ModBlocksAndItems.BURNED_GRASS.tallGrass,
            ModBlocksAndItems.DRIED_GRASS.grassBlock,
            ModBlocksAndItems.DRIED_GRASS.grass,
            ModBlocksAndItems.DRIED_GRASS.tallGrass,
            ModBlocksAndItems.SMOLDERING_EMBERS,
            ModBlocksAndItems.SMOKING_EMBERS,
            ModBlocksAndItems.FLICKERING_LANTERN,
            ModBlocksAndItems.FLICKERING_SOUL_LANTERN,
            ModBlocksAndItems.EXTINGUISHED_LANTERN,
            ModBlocksAndItems.FANCY_COBWEB,
            ModBlocksAndItems.FANCY_COBWEB_WITH_SPIDER_NEST,
            ModBlocksAndItems.FANCY_CORNER_COBWEB,
            ModBlocksAndItems.CORNER_COBWEB,
            *ModBlocksAndItems.NOCTISTEEL.getCutoutBlocks(),
            ModBlocksAndItems.WALL_COBWEB,
            ModBlocksAndItems.SPIDER_SILK,
            ModBlocksAndItems.SPIDER_SILK_STRAND
        )

        BlockEntityRendererFactories.register(ModBlockEntities.SPIDER_EGG_BLOCK_ENTITY, ::SpiderEggClusterRenderer)

        EntityRendererRegistry.register(ModEntities.ARACHNE, ::ArachneEntityRenderer)
        EntityRendererRegistry.register(ModEntities.SPIDERLING, ::SpiderlingEntityRenderer)
        EntityRendererRegistry.register(ModEntities.SPIDERLING_ALLY, ::SpiderlingEntityRenderer)
        EntityRendererRegistry.register(ModEntities.CHARMING_SPIDER, ::CharmingSpiderEntityRenderer)
        EntityRendererRegistry.register(ModEntities.WEB_CASTER, ::WebCasterEntityRenderer)
        EntityRendererRegistry.register(ModEntities.DADDY_LONG_LEGS, ::DaddyLongLegsEntityRenderer)
        EntityRendererRegistry.register(ModEntities.JUMPING_SPIDER, ::JumpingSpiderEntityRenderer)
        EntityRendererRegistry.register(ModEntities.SPINY_SPIDER, ::SpinySpiderEntityRenderer)
        EntityRendererRegistry.register(ModEntities.ARACHNE_EGG, ::ArachneEggRenderer)
        EntityRendererRegistry.register(ModEntities.SPIDER_SILK_BOLA, ::SpiderSilkBolaRenderer)
        EntityRendererRegistry.register(ModEntities.SPIDER_EGG_PROJECTILE, ::SpiderEggProjectileRenderer)

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { _, _, _, context ->
            if (!customRenderersWorkaroundInit) {
                customRenderersWorkaroundInit = true
                initArmor(context)
            }
        }
    }

    private fun initArmor(context: EntityRendererFactory.Context) {
        ArmorRenderer.register(NetwalkersRenderer(context), ModBlocksAndItems.NETWALKERS)
    }

    private fun registerLayer(provider: TexturedModelDataProvider, path: String, name: String = "main") =
        EntityModelLayer(Derelict.id(path), name).also {
            EntityModelLayerRegistry.registerModelLayer(it, provider)
        }
}