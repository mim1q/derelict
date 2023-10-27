package dev.mim1q.derelict

import dev.mim1q.derelict.init.ModParticles
import dev.mim1q.derelict.init.client.ModRender
import dev.mim1q.derelict.item.CrosshairTipItem
import dev.mim1q.derelict.util.BlockMarkerUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items

object DerelictClient : ClientModInitializer {
  override fun onInitializeClient() {
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
      MinecraftClient.getInstance().player?.let {player ->
        listOf(player.mainHandStack.item, player.offHandStack.item)
      }?.forEach { item ->
        if (item != null && item is CrosshairTipItem || item == Items.HONEYCOMB) {
          BlockMarkerUtils.renderCrosshairTip(context, item as CrosshairTipItem)
          return@register
        }
      }
    }

    LivingEntityFeatureRendererRegistrationCallback.EVENT.register {
      _, _, _, ctx -> BlockMarkerUtils.setupMarkerModel(ctx.modelLoader)
    }

    WorldRenderEvents.AFTER_ENTITIES.register(BlockMarkerUtils::drawMarkers)
  }
}