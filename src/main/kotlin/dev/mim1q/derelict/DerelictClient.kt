package dev.mim1q.derelict

import com.mojang.blaze3d.systems.RenderSystem
import dev.mim1q.derelict.client.render.block.SpiderEggClusterRenderer
import dev.mim1q.derelict.client.render.effect.SpiderWebModelFeature
import dev.mim1q.derelict.config.DerelictClientConfig
import dev.mim1q.derelict.config.DerelictConfigs
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModParticles
import dev.mim1q.derelict.init.ModStatusEffects
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.init.component.ModCardinalComponents.getClientSyncedEffectAmplifier
import dev.mim1q.derelict.init.component.ModCardinalComponents.hasClientSyncedEffect
import dev.mim1q.derelict.interfaces.AbstractBlockAccessor
import dev.mim1q.derelict.item.CrosshairTipItem
import dev.mim1q.derelict.util.BlockMarkerUtils
import dev.mim1q.gimm1q.client.highlight.HighlightDrawerCallback
import dev.mim1q.gimm1q.interpolation.Easing
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import kotlin.math.sin

object DerelictClient : ClientModInitializer {
    val CLIENT_CONFIG: DerelictClientConfig = DerelictConfigs.CLIENT_CONFIG

    override fun onInitializeClient() {
        CLIENT_CONFIG.save()

        ModRender.init()
        ModParticles.initClient()

        // Disabled for now - it's a bit hard to make a spider filter when there is a spider boss planned ;)
        // Might figure something out in the future (turn the spiders into robots in the resrouce pack?)

        // ResourceManagerHelper.registerBuiltinResourcePack(
        //   Derelict.id("arachnophobia_filter"),
        //   FabricLoader.getInstance().getModContainer("derelict").get(),
        //   Text.literal("Arachnophobia Filter"),
        //   ResourcePackActivationType.NORMAL,
        // )

        HudRenderCallback.EVENT.register { context, _ ->
            MinecraftClient.getInstance().player?.let { player ->
                listOf(player.mainHandStack.item, player.offHandStack.item)
            }?.forEach { item ->
                if (item != null && item is CrosshairTipItem || item == Items.HONEYCOMB) {
                    BlockMarkerUtils.renderCrosshairTip(context, item as CrosshairTipItem)
                    return@register
                }
            }
        }

        val hudWebTextures = arrayOf(
            Derelict.id("textures/gui/effect/spider_web_gui_sparse.png"),
            Derelict.id("textures/gui/effect/spider_web_gui.png"),
            Derelict.id("textures/gui/effect/spider_web_gui_dense.png")
        )

        fun DrawContext.drawFullScreenTexture(texture: Identifier, ratio: Float) {
            val windowWidth = MinecraftClient.getInstance().window.scaledWidth
            val windowHeight = MinecraftClient.getInstance().window.scaledHeight
            val isHorizontal = (windowWidth.toFloat() / windowHeight) > ratio

            val width = if (isHorizontal) windowWidth else (windowHeight * ratio).toInt()
            val height = if (isHorizontal) (windowWidth / ratio).toInt() else windowHeight

            RenderSystem.enableBlend()
            drawTexture(
                texture,
                (windowWidth - width) / 2, (windowHeight - height) / 2,
                0f, 0f,
                width, height,
                width, height
            )
        }

        HudRenderCallback.EVENT.register { context, _ ->
            val player = MinecraftClient.getInstance().player ?: return@register
            val camera = MinecraftClient.getInstance().gameRenderer.camera
            val world = player.world

            val light = Easing.lerp(world.getBrightness(camera.blockPos) + 1 / 16f, 1f, 0.2f)
            val lastShaderColor = RenderSystem.getShaderColor()

//            RenderSystem.setShaderColor(light, light, light, 1f)

            if (player.hasClientSyncedEffect(ModStatusEffects.COBWEBBED)) {
                val level = MathHelper.clamp(player.getClientSyncedEffectAmplifier(ModStatusEffects.COBWEBBED) ?: 0, 0, 2)
                context.drawFullScreenTexture(hudWebTextures[level], 16 / 9f)
            }

            RenderSystem.setShaderColor(lastShaderColor[0], lastShaderColor[1], lastShaderColor[2], lastShaderColor[3])
        }


        HighlightDrawerCallback.EVENT.register { drawer, ctx ->
            val range = CLIENT_CONFIG.waxableAndAgeableHightlightRange()
            if (range <= 0) return@register
            val stack = ctx.player.mainHandStack

            val waxing = CLIENT_CONFIG.waxableHighlights()
                && (stack.isOf(ModBlocksAndItems.WAXING_STAFF) || stack.isOf(Items.HONEYCOMB))
            val aging = CLIENT_CONFIG.ageableHighlights() && stack.isOf(ModBlocksAndItems.AGING_STAFF)

            if (!aging && !waxing) return@register

            val world = ctx.player.world
            val tickDelta = MinecraftClient.getInstance().tickDelta
            val opacity = (sin((world.time + tickDelta) * 0.25F) * 32F + 32F).toInt()
            val color = (opacity shl 24) or 0xfcad03

            BlockPos.iterateOutwards(ctx.player.blockPos, range, range, range).forEach {
                val block = world.getBlockState(it).block as AbstractBlockAccessor
                if (
                    (aging && block.isAgeable)
                    || (waxing && block.isWaxable)
                ) {
                    drawer.highlightBlock(it, color, 0x80fcad03.toInt())
                }
            }
        }

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register { _, entityRenderer, registrationHelper, _ ->
            if (entityRenderer is LivingEntityRenderer) {
                @Suppress("UNCHECKED_CAST")
                registrationHelper.register(SpiderWebModelFeature(entityRenderer as LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>))
            }
        }

        ModelLoadingPlugin.register { ctx ->
            ctx.addModels(*SpiderEggClusterRenderer.MODEL_IDS)
        }
    }
}