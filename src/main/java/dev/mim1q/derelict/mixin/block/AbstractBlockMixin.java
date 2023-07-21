package dev.mim1q.derelict.mixin.block;

import dev.mim1q.derelict.interfaces.AbstractBlockAccessor;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin implements AbstractBlockAccessor {
  @Unique
  private boolean waxable = false;
  @Unique
  private boolean ageable = false;

  @Override
  public boolean isWaxable() {
    return waxable;
  }

  @Override
  public void setWaxable(boolean value) {
    waxable = value;
  }

  @Override
  public boolean isAgeable() {
    return ageable;
  }

  @Override
  public void setAgeable(boolean value) {
    ageable = value;
  }
}
