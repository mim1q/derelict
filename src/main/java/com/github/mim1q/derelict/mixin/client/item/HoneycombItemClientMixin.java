package com.github.mim1q.derelict.mixin.client.item;

import com.github.mim1q.derelict.item.CrosshairTipItem;
import net.minecraft.block.Block;
import net.minecraft.item.HoneycombItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HoneycombItem.class)
public class HoneycombItemClientMixin implements CrosshairTipItem {
  private static final Identifier TEXTURE = new Identifier("textures/item/honeycomb.png");
  private Block lastBlock = null;
  private boolean didShowTip = false;

  @Override
  public boolean shouldShowTip(@Nullable Block block) {
    if (block == null) return false;
    if (block == lastBlock) return didShowTip;
    lastBlock = block;
    didShowTip = HoneycombItem.UNWAXED_TO_WAXED_BLOCKS.get().containsKey(block);
    return didShowTip;
  }

  @NotNull
  @Override
  public Identifier getTipTexture() {
    return TEXTURE;
  }
}
