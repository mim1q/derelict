package dev.mim1q.derelict

import dev.mim1q.derelict.config.DerelictClientConfig
import dev.mim1q.derelict.config.DerelictConfigs
import dev.mim1q.derelict.init.ModBlocksAndItems
import dev.mim1q.derelict.init.ModParticles
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.interfaces.AbstractBlockAccessor
import dev.mim1q.derelict.item.CrosshairTipItem
import dev.mim1q.derelict.util.BlockMarkerUtils
import dev.mim1q.gimm1q.client.highlight.HighlightDrawerCallback
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.util.math.BlockPos
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
    }
}