package dev.mim1q.derelict.mixin.client.item;

import dev.mim1q.derelict.Derelict;
import dev.mim1q.derelict.item.CrosshairTipItem;
import net.minecraft.block.Block;
import net.minecraft.item.HoneycombItem;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HoneycombItem.class)
public class HoneycombItemClientMixin implements CrosshairTipItem {
  @Unique
  private static final Identifier TEXTURE = new Identifier("textures/item/honeycomb.png");
  @Unique
  private Block lastBlock = null;
  @Unique
  private boolean didShowTip = false;

  @Override
  public boolean shouldShowTip(@Nullable Block block) {
    if (block == null || !Derelict.INSTANCE.getCLIENT_CONFIG().waxableCrosshairTip()) return false;
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
