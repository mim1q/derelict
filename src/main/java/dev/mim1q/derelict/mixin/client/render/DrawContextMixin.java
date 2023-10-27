package dev.mim1q.derelict.mixin.client.render;

import dev.mim1q.derelict.Derelict;
import dev.mim1q.derelict.item.tag.ModItemTags;
import dev.mim1q.derelict.util.BlockMarkerUtils;
import kotlin.Unit;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

  @Shadow abstract void drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha);

  @Shadow @Final private MatrixStack matrices;

  @Inject(
    method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
    at = @At("TAIL")
  )
  void derelict$injectDrawItem(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
    if (
      stack.isIn(ModItemTags.INSTANCE.getWAXED_COMMON())
      && Derelict.INSTANCE.getCLIENT_CONFIG().waxedIndicatorOpacity() > 0F
    ) {
      BlockMarkerUtils.INSTANCE.renderWaxedIndicator(this::derelict$drawTexture, x, y);
    }
  }

  @Unique
  public Unit derelict$drawTexture(Identifier texture, int x, int y, int z, int size, float alpha) {
    this.matrices.push();
    this.matrices.translate(0.0F, 0.0F, z);
    drawTexturedQuad(
      texture, x, x + size, y, y + size, 0, 0, 1, 0, 1, 1F, 1F, 1F, alpha
    );
    this.matrices.pop();
    return Unit.INSTANCE;
  }
}
