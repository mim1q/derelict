package com.github.mim1q.derelict.mixin.client.render;

import com.github.mim1q.derelict.item.tag.ModItemTags;
import com.github.mim1q.derelict.util.RenderUtil;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
  @Inject(method = "renderGuiItemModel", at = @At("TAIL"))
  void derelict$injectRenderGuiItemModel(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
    if (stack.isIn(ModItemTags.INSTANCE.getWAXED_COMMON())) {
      RenderUtil.INSTANCE.renderWaxedIndicator(x - 2, y - 2, 8);
    }
  }
}
