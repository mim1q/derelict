package com.github.mim1q.derelict.mixin.block;

import com.github.mim1q.derelict.block.tag.ModBlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
abstract class AbstractBlockStateMixin {
  @Shadow public abstract boolean isIn(TagKey<Block> tag);

  @Inject(
    method = "getModelOffset",
    at = @At("HEAD"),
    cancellable = true
  )
  public void derelict$injectGetModelOffset(BlockView world, BlockPos pos, CallbackInfoReturnable<Vec3d> cir) {
    if (this.isIn(ModBlockTags.INSTANCE.getCOVER_BOARDS())) {
      var x = pos.getX() % 2;
      var y = pos.getY() % 2;
      var z = pos.getZ() % 2;
      cir.setReturnValue(
        new Vec3d(
          (z * 0.0015) + (y * 0.001),
          (x * 0.0015) + (z * 0.001),
          (y * 0.0015) + (x * 0.001)
        )
      );
    }
  }
}